package com.example.week4

import android.R
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.week4.databinding.ActivitySongBinding
import kotlinx.coroutines.selects.SelectInstance

class SongActivity: AppCompatActivity(){

    lateinit var binding: ActivitySongBinding
    override fun onCreate(savedInstaneState:Bundle?){
        super.onCreate(savedInstaneState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
    }
    fun setPlayerStatus(isNotPlaying: Boolean){
        if(isNotPlaying){
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }else{
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
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
    fun setRandomStatus(isSelected:Boolean){
        if(isSelected){
            binding.songRandomIv.visibility = View.INVISIBLE
            binding.songRandomSelectedIv.visibility = View.VISIBLE
        }else{
            binding.songRandomIv.visibility = View.VISIBLE
            binding.songRandomSelectedIv.visibility = View.INVISIBLE
        }
    }
}