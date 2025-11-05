package com.example.week6

import android.R.attr.text
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.week6.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySongBinding
    lateinit var song : Song
    lateinit var timer : Timer


    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

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

        initSong()
        setPlayer(song)

//        val musicServiceIntent = Intent(this, MusicService::class.java).apply {
//            putExtra("songTitle", song.title)
//            putExtra("songArtist", song.singer)
//            putExtra("isPlaying", true)
//        }
//        ContextCompat.startForegroundService(this, musicServiceIntent)
//        bindService(musicServiceIntent, connection, Context.BIND_AUTO_CREATE)


        // 다음곡 버튼, 이전곡 버튼 터치 시 곡 처음부터 시작
        binding.songPreviousIv.setOnClickListener {
            restartSong()
        }

        binding.songNextIv.setOnClickListener {
            restartSong()
        }


        // 일시정지, 재생버튼 토글
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
//            musicService?.playMusic()
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
//            musicService?.pauseMusic()
        }

        binding.songDownIb.setOnClickListener{
            finish()
        }
    }

    private fun restartSong(){
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
        initSong()
        setPlayer(song)
        setPlayerStatus(true)
    }
    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0)!!,
                intent.getIntExtra("playTime", 0)!!,
                intent.getBooleanExtra("isPlaying", false)!!,
                intent.getStringExtra("music")!!
            )
        }

        startTimer()
    }

    private fun setPlayer(song : Song){
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second/60, song.second%60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
        binding.songProgressSb.progress = (song.second/song.playTime) * 1000
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        if (music == 0) {
            Log.e("SongActivity", "Resource not found: ${song.music}")
        } else {
            mediaPlayer = MediaPlayer.create(this, music)
        }

        setPlayerStatus(song.isPlaying)
    }
    private fun setPlayerStatus(isPlaying : Boolean){
        song.isPlaying = isPlaying
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
                mediaPlayer?.stop()
            }
        }
    }


    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying)
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
        setPlayerStatus(false)
        song.second = ((binding.songProgressSb.progress * song.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)
        editor.apply()
    }
    override fun onDestroy(){
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}