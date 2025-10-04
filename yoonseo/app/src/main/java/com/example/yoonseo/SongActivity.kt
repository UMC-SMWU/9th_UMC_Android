package com.example.yoonseo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.yoonseo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로 받은 값 꺼내기
        val title = intent.getStringExtra("title") ?:"Unknown Title"
        val singer = intent.getStringExtra("singer") ?: "Unknown Singer"
        val albumResId = intent.getIntExtra("albumResId", 0)

        // 뷰에 반영하기
        binding.songMusicTitleTv.text = title
        binding.songSingerNameTv.text = singer
        if(albumResId != 0) {
            binding.songAlbumIv.setImageResource(albumResId)
        }

        // 닫기 버튼
        binding.songDownIb.setOnClickListener {
            finish()
        }

    }
}