package com.example.week9_10.ui.song

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.week9_10.R
import com.example.week9_10.SongDatabase
import com.example.week9_10.data.entities.Song
import com.example.week9_10.databinding.ActivitySongBinding

class SongActivity: AppCompatActivity(){

    lateinit var binding: ActivitySongBinding
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer?= null
    val songs = arrayListOf<Song>()
    private var songDB: SongDatabase? = null
    var nowPos = 0

    override fun onCreate(savedInstaneState: Bundle?){
        super.onCreate(savedInstaneState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayerList()
        initSong()
        initClickListener()
    }
    private fun initPlayerList(){
        songDB = SongDatabase.Companion.getInstance(this)
        songDB?.songDao()?.getSongs()?.let { songs.addAll(it) }
    }
    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        nowPos = getPlayingSongPosition(songId)
        startTimer()
        setPlayer(songs[nowPos])
    }
    private fun moveSong(direction: Int){
        if(nowPos + direction <0){
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direction >= songs.size){
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }
        nowPos += direction

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null
        setPlayer(songs[nowPos])
    }
    private fun getPlayingSongPosition(songId: Int): Int{
        for(i in 0 until songs.size){
           if(songs[i].id == songId){
               return i
           }
        }
        return 0
    }
    private fun initClickListener(){
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(songs[nowPos].isPlaying, songs[nowPos].isLike)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(songs[nowPos].isPlaying, songs[nowPos].isLike)
        }
        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }
        binding.songNextIv.setOnClickListener {
            moveSong(+1)
        }
        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(false, songs[nowPos].isLike)
        }
        binding.songPauseIv.setOnClickListener { setPlayerStatus(true, songs[nowPos].isLike) }
        binding.songRepeatIv.setOnClickListener { setRepeatStatus(true) }
        binding.songRepeatSelectedIv.setOnClickListener { setRepeatStatus(false) }
        binding.songRandomIv.setOnClickListener { setRandomStatus(true) }
        binding.songRandomSelectedIv.setOnClickListener { setRandomStatus(false) }
    }
    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB?.songDao()?.updateIsLikeById(!isLike, songs[nowPos].id)
        if(!isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second/60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.albumCover)
        binding.songProgressbarSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        setPlayerStatus(song.isPlaying, song.isLike)
    }
    fun setPlayerStatus(isNotPlaying: Boolean, isLike: Boolean){

        if(isNotPlaying){
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            songs[nowPos].isPlaying = false
            timer.isPlaying = false
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }else{
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            songs[nowPos].isPlaying = true
            timer.isPlaying = true
            mediaPlayer?.start()
        }

        if(isLike)
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        else
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
    }
    fun setRepeatStatus(isSelected:Boolean){
        if(isSelected){
            binding.songRepeatIv.visibility = View.INVISIBLE
            binding.songRepeatSelectedIv.visibility = View.VISIBLE
        }else{
            binding.songRepeatIv.visibility = View.VISIBLE
            binding.songRepeatSelectedIv.visibility = View.INVISIBLE
        }
    }
    fun repeatSong(){
        if(!songs[nowPos].isPlaying){
            setPlayerStatus(songs[nowPos].isPlaying, songs[nowPos].isLike)
        }
        binding.songStartTimeTv.text="00:00"
        binding.songProgressbarSb.progress = 0
        mediaPlayer?.seekTo(0)
        mediaPlayer?.start()
        timer.interrupt()
        startTimer()
    }
    fun setRandomStatus(isSelected:Boolean){
        if(isSelected){
            binding.songRandomIv.visibility = View.INVISIBLE
            binding.songRandomSelectedIv.visibility = View.VISIBLE
        }else{
            binding.songRandomIv.visibility = View.VISIBLE
            binding.songRandomSelectedIv.visibility = View.INVISIBLE
        }
    }
    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    override fun onPause() {
        super.onPause()
        setPlayerStatus(false, songs[nowPos].isLike)
        songs[nowPos].second = ((binding.songProgressbarSb.progress * songs[nowPos].playTime)/100)/1000
        // 내부 저장소에 데이터 저장해 앱 종료 후 다시 실행 시 저장된 데이터 꺼내서 사용 가능
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("songId", songs[nowPos].id)
        editor.putInt("songSecond", songs[nowPos].second)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()      // 갖고 있던 리소스 해제
        mediaPlayer = null          // mediaPlayer 해제
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread(){
        private var second: Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try{
                while(true){
                    if(second >= playTime){
                        songs[nowPos].isPlaying = false
                        runOnUiThread {
                            binding.songProgressbarSb.progress = 0
                            binding.songStartTimeTv.text = "00:00"
                            binding.songMiniplayerIv.visibility = View.VISIBLE
                            binding.songPauseIv.visibility = View.GONE
                        }
                        break
                    }
                    if(isPlaying){
                        sleep(50)
                        mills += 50
                        runOnUiThread {
                            binding.songProgressbarSb.progress = ((mills / playTime) * 100).toInt()
                        }
                        if(mills % 1000 == 0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            }catch (e: InterruptedException){
                Log.d("Song", "The thread is dead ${e.message}")
            }
        }
    }
}