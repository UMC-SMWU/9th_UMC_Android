package com.example.week4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.week4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString())
        Log.d("Song", song.title + song.singer)
        binding.mainPlayerCl.setOnClickListener{
            //startActivity(Intent(this, SongActivity::class.java))
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
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
}