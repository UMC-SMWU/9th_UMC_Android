package com.example.week6

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.week6.databinding.ActivityWeek6Binding
import com.google.gson.Gson

import kotlin.jvm.java

class Week6 : AppCompatActivity() {
    private lateinit var binding: ActivityWeek6Binding

    private var song:Song = Song()
    private var gson: Gson = Gson()
    private var flag_return_songActivity = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityWeek6Binding.inflate(layoutInflater)
        setContentView(binding.root)


        Log.d("Song",song.title + song.singer)

        // 다른 액티비티에서 돌아올 때 데이터 받는 부분 // 이부분은 onStart()로 옮겨서 songActivityLauncher말고 startActivity()로 시작함
//        val songActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//                val data = result.data
//                val title = data?.getStringExtra("title") ?: ""
//                val singer = data?.getStringExtra("singer") ?: ""
//                Toast.makeText(this, "노래: $title ($singer)", Toast.LENGTH_SHORT).show()
//                }
//        }

        // 메인 플레이어 눌렀을 때 인텐트에 정보 담아서 보내기
        binding.mainPlayerCl.setOnClickListener {
            flag_return_songActivity = true
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("music", song.music)
            startActivity(intent) // 이부분이 songActivityLauncher였음
        }

        // BottomNavView에 프래그먼트 할당하기
        binding.mainBnv.setOnItemSelectedListener { menuItem ->
            changeFragment(
                when (menuItem.itemId) {
                    R.id.homeFragment -> HomeFragment()
                    else -> LockerFragment()
                }
            )
            true
        }


        binding.mainMiniplayerBtn.setOnClickListener {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        }
        binding.mainPauseBtn.setOnClickListener {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
        // 일단 시작은 홈 프래그먼트를 띄우기
        supportFragmentManager.beginTransaction()
            .replace(binding.mainFrm.id, HomeFragment())
            .commit()

    }

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(binding.mainFrm.id, fragment)
            .commitAllowingStateLoss()

    }
    fun setPlayerData(album: Album) {
        binding.mainMiniplayerTitleTv.text = album.title
        binding.mainMiniplayerSingerTv.text = album.singer
        // 그다음에 노래 재생 현황같은것도 넘겨줄수 있어야함
        binding.mainMiniplayerBtn.visibility = View.GONE
        binding.mainPauseBtn.visibility = View.VISIBLE
    }

    private fun setMiniPlayer(song: Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.songProgressSb.progress = (song.second*100000)/song.playTime
    }
    override fun onStart(){
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)

        song = if(songJson == null){
            Song("라일락", "아이유(IU)", 0, 60, false, "music_lilac")
        }else{
            gson.fromJson(songJson, Song::class.java)
        }

        // 처음 앱 실행했을 때는 토스트 메새지 안나오게 하기
        if (flag_return_songActivity) {
            Toast.makeText(this, "노래: ${song.title} (${song.singer})", Toast.LENGTH_SHORT).show()
            flag_return_songActivity = false
        }

        setMiniPlayer(song)
    }
}