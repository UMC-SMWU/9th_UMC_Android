package com.example.week4

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.week4.databinding.ActivitySongBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class SongActivity: AppCompatActivity(){
    // TODO: 노래 끝나고 플레이 버튼 누르면 서비스 재시작
    // TODO: SongActivity로 다시 돌아올 때 시간 초기화 되지 않도록 수정

    lateinit var binding: ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer

    private var musicService: MusicService? = null
    private var isBound = false
    private var updateJob: Job? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true
            //updateUI()
            //updateSeekbar()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

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

        // 서비스 시작
        val musicServiceIntent = Intent(this, MusicService::class.java).apply {
            putExtra("title", song.title)
            putExtra("singer", song.singer)
            putExtra("isPlaying", true)
        }
        ContextCompat.startForegroundService(this, musicServiceIntent)
        bindService(musicServiceIntent, connection, Context.BIND_AUTO_CREATE)

        binding.songMiniplayerIv.setOnClickListener {
            musicService?.playMusic()
            setPlayerStatus(song.isPlaying)
        }
        binding.songPauseIv.setOnClickListener {
            musicService?.pauseMusic()
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
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
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

        setPlayerStatus(song.isPlaying)
    }
    fun setPlayerStatus(isNotPlaying: Boolean){

        if(isNotPlaying){
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            song.isPlaying = false
            timer.isPlaying = false
        }else{
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            song.isPlaying = true
            timer.isPlaying = true
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
        musicService?.seekTo(0)
        musicService?.playMusic()
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

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread(){
        private var second: Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try{
                while(true){
                    if(second >= playTime){
                        musicService?.pauseMusic()
                        musicService?.seekTo(0)
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