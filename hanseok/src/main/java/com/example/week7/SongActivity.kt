package com.example.week7

import android.R.attr.text
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.week7.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.util.Timer

class SongActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySongBinding
    lateinit var timer : Timer
    var nowPos = 0

    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase

//    private val connection = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            val binder = service as MusicService.MusicBinder
//            musicService = binder.getService()
//            isBound = true
//        }
//
//        override fun onServiceDisconnected(name: ComponentName?) {
//            isBound = false
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()

//        val musicServiceIntent = Intent(this, MusicService::class.java).apply {
//            putExtra("songTitle", song.title)
//            putExtra("songArtist", song.singer)
//            putExtra("isPlaying", true)
//        }
//        ContextCompat.startForegroundService(this, musicServiceIntent)
//        bindService(musicServiceIntent, connection, Context.BIND_AUTO_CREATE)


    }


    private fun initClickListener(){
        // 일시정지, 재생버튼 토글
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        // musicService?.playMusic()
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        // musicService?.pauseMusic()
        }

        binding.songDownIb.setOnClickListener{
            finish()
        }

        // 다음곡 버튼, 이전곡 버튼 터치 시 곡 처음부터 시작
        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }

        binding.songNextIv.setOnClickListener {
            moveSong(1)
        }

        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }
    private fun setPlayerStatus(isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else{
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }
    private fun setLike(isLike : Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeByID(!isLike, songs[nowPos].id)

        if(!isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }
    private fun moveSong(direct: Int){
        if(nowPos + direct < 0){
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direct >= songs.size){
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }
        nowPos += direct

        timer.interrupt()
        startTimer()
        mediaPlayer?.release()
        mediaPlayer = null
        setPlayer(songs[nowPos])
    }
    private fun restartSong(){
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
        initSong()
        setPlayer(songs[nowPos])
        setPlayerStatus(true)
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.clear()
        songs.addAll(songDB.songDao().getSongs())
    }
    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int) : Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId) return i
        }
        return 0
    }
    private fun setPlayer(song : Song){
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second/60, song.second%60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressSb.progress = (song.second/song.playTime) * 1000
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        if (music == 0) {
            Log.e("SongActivity", "Resource not found: ${song.music}")
        } else {
            mediaPlayer = MediaPlayer.create(this, music)
            mediaPlayer?.seekTo(song.second * 1000)
        }

        if(song.isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying : Boolean = true) : Thread(){
        private var second : Int = 0
        private var mills : Float = 0f

        override fun run(){
            super.run()

            try{
                while(true){
                    if(second >= playTime) break;
                    if(isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime) * 100).toInt()
                        }
                        if(mills % 1000 == 0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d", second/60, second%60)
                            }
                            second++
                        }
                    }
                }
            }catch(e: InterruptedException){
                Log.d("Song", "쓰레드가 죽었습니다. ${e.message}")
            }

        }
    }

    override fun onPause(){
        super.onPause()
        //progressbar에 text는 아직 업데이트 안했지만
        // main과 songActivity사이에 시간 공유 부분
        songs[nowPos].second += ((binding.songProgressSb.progress * songs[nowPos].playTime)/100)/1000
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
        songDB.songDao().update(songs[nowPos])
    }
    override fun onDestroy(){
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}