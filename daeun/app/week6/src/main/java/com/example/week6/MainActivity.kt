package com.example.week6

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.week6.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var song:Song? = null
    private var gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        setTheme(R.style.Theme_UMC_Study)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainPlayerCl.setOnClickListener{

            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song?.title)
            intent.putExtra("singer", song?.singer)
            intent.putExtra("second", song?.second)
            intent.putExtra("second", song?.second)
            intent.putExtra("playTime", song?.playTime)
            intent.putExtra("isPlaying", song?.isPlaying)
            intent.putExtra("music", song?.music)
            startActivity(intent)
        }

        initBottomNavigation()
    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            changeFragment(item)
        }
    }
    private fun changeFragment(item: MenuItem): Boolean{
        when (item.itemId) {

            R.id.homeFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment())
                    .commitAllowingStateLoss()
                return true
            }

            R.id.lookFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, LookFragment())
                    .commitAllowingStateLoss()
                return true
            }
            R.id.searchFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, SearchFragment())
                    .commitAllowingStateLoss()
                return true
            }
            R.id.lockerFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, LockerFragment())
                    .commitAllowingStateLoss()
                return true
            }
        }
        return false
    }

    private fun setMiniPlayer(song: Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressbarSb.progress = (song.second*100000)/song.playTime
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)
        song = if(songJson == null){
            Song("LILAC", "아이유(IU)", 0, 117, false, "running_night_393139")
        }else{
            gson.fromJson(songJson, Song::class.java)
        }
        setMiniPlayer(song!!)
    }
}