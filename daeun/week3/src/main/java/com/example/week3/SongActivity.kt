package com.example.week3

import android.R
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.week3.databinding.ActivitySongBinding
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
    }
    fun setPlayerStatus(isPlaying: Boolean){
        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }else{
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
    }
}