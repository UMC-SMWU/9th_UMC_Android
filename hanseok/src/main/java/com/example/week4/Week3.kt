package com.example.week4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.week4.databinding.ActivityWeek4Binding

import kotlin.jvm.java

class Week4 : AppCompatActivity() {
    private lateinit var binding: ActivityWeek4Binding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityWeek4Binding.inflate(layoutInflater)
        setContentView(binding.root)
        // Song이라는 데이터클래스의 인스턴스 생성하여 미니플래이어에있는 곡 정보 넣기
        val song = Song(
            order = 0,
            binding.mainMiniplayerTitleTv.text.toString(),
            binding.mainMiniplayerSingerTv.text.toString()
        )
        Log.d("Song",song.title + song.singer)

        // 다른 액티비티에서 돌아올 때 데이터 받는 부분
        val songActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val title = data?.getStringExtra("title") ?: ""
                val singer = data?.getStringExtra("singer") ?: ""
                Toast.makeText(this, "노래: $title ($singer)", Toast.LENGTH_SHORT).show()
            }
        }

        // 메인 플레이어 눌렀을 때 인텐트에 정보 담아서 보내기
        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            songActivityLauncher.launch(intent)
        }
        // BottomNavView에 프래그먼트 할당하기
        binding.mainBnv.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId){
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.mainFrm.id, HomeFragment())
                        .commit()
                    true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.mainFrm.id, LockerFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
        // 일단 시작은 홈 프래그먼트를 띄우기
        supportFragmentManager.beginTransaction()
            .replace(binding.mainFrm.id, HomeFragment())
            .commit()

    }

}