package com.example.week6

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.week6.databinding.ActivitySongBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class SongActivity: AppCompatActivity(){

    lateinit var binding: ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer?= null
    private var gson = Gson()

    override fun onCreate(savedInstaneState:Bundle?){
        super.onCreate(savedInstaneState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songPauseIv.setOnClickListener { setPlayerStatus(true) }
        binding.songRepeatIv.setOnClickListener { setRepeatStatus(true) }
        binding.songRepeatSelectedIv.setOnClickListener { setRepeatStatus(false) }
        binding.songRandomIv.setOnClickListener { setRandomStatus(true) }
        binding.songRandomSelectedIv.setOnClickListener { setRandomStatus(false) }

        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(song.isPlaying)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(song.isPlaying)
        }
        binding.songPreviousIv.setOnClickListener {
            repeatSong()
        }
        binding.songNextIv.setOnClickListener {
            repeatSong()
        }

    }
    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title"),
                intent.getStringExtra("singer"),
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getStringExtra("music")
            )
        }
        startTimer()
    }
    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")
        binding.songSingerNameTv.text = intent.getStringExtra("singer")
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second/60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime % 60)
        binding.songProgressbarSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        setPlayerStatus(song.isPlaying)
    }
    fun setPlayerStatus(isNotPlaying: Boolean){

        if(isNotPlaying){
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            song.isPlaying = false
            timer.isPlaying = false
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }else{
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            song.isPlaying = true
            timer.isPlaying = true
            mediaPlayer?.start()
        }
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
        if(!song.isPlaying){
            setPlayerStatus(song.isPlaying)
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
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.songProgressbarSb.progress * song.playTime)/100)/1000
        // 내부 저장소에 데이터 저장해 앱 종료 후 다시 실행 시 저장된 데이터 꺼내서 사용 가능
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)
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
                        song.isPlaying = false
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