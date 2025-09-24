package com.example.floapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.floapp.databinding.ActivitySongBinding


class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val songTitle = intent.getStringExtra("songTitle")
        binding.albumSongTitle.text = songTitle
        binding.songPlayerShuffle.setOnClickListener { item ->
            when(item.id){
                R.id.song_player_shuffle -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragmentContainer, AlbumFragment())
                        .commit()
                    binding.songDefaultView.visibility = View.INVISIBLE
                }
            }
        }
        binding.songArrow.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java).apply{
                putExtra(MainActivity.STRING_INTENT_KEY, "defaultSongTitle")
            }
            setResult(Activity.RESULT_OK, intent)
            if(!isFinishing) finish()
        }
    }
}