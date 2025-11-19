package com.example.week8

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.week8.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity() : AppCompatActivity() {
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

        inputDummySongs()
        inputDummyAlbums()

        binding.mainPlayerCl.setOnClickListener{
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            song?.id?.let { editor.putInt("songId", it) }
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
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

    private fun setMiniPlayer(song: Song?, songSecond: Int){
        binding.mainMiniplayerTitleTv.text = song?.title
        binding.mainMiniplayerSingerTv.text = song?.singer
        song?.playTime?.let { binding.mainProgressbarSb.progress = (songSecond*100000)/it }
    }

    override fun onStart() {
        super.onStart()
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        val songSecond = spf.getInt("songSecond", 0)
        val songDB = SongDatabase.getInstance(this)

        song = if(songId == 0){
            songDB?.songDao()?.getSong(1)
        }else{
            songDB?.songDao()?.getSong(songId)
        }
        Log.d("Song ID", song?.id.toString())
        setMiniPlayer(song, songSecond)
    }

    private fun inputDummyAlbums(){
        val songDB = SongDatabase.getInstance(this)
        val albums = songDB?.albumDao()?.getAlbums()

        if(albums?.isNotEmpty() == true) return

        songDB?.albumDao()?.insert(
            Album(0,"IU 5th Album LILAC", "IU", R.drawable.img_album_exp2)
        )
        songDB?.albumDao()?.insert(
            Album(1,"BUTTER", "BTS", R.drawable.img_album_exp)
        )
        songDB?.albumDao()?.insert(
            Album(2,"Pixabay Song", "Many Singers", R.drawable.img_album_exp2)
        )
        songDB?.albumDao()?.insert(
            Album(3,"IU 5th Album LILAC", "IU", R.drawable.img_album_exp2)
        )
    }
    private fun inputDummySongs(){
        // inputDummyAlbum과 유사하게 작성
        val songDB = SongDatabase.getInstance(this)
        val songs = songDB?.songDao()?.getSongs()

        if(songs?.isNotEmpty() == true) return

        songDB?.songDao()?.insert(
            Song(
                "Background Music", "DELOSound", 0, 142,
                false, "background_music",
                R.drawable.img_album_exp, false, 2
            )
        )
        songDB?.songDao()?.insert(
            Song(
                "Christmas Holiday Festive Magic", "Top-Flow", 0, 79,
                false, "christmas_holiday_festive_magic",
                R.drawable.img_album_exp, false, 2
            )
        )
        songDB?.songDao()?.insert(
            Song(
                "Lofi Background Music", "DELOSound", 0, 92,
                false, "lofi_background_music",
                R.drawable.img_album_exp, false, 2
            )
        )
        songDB?.songDao()?.insert(
            Song(
                "Running Night", "Alex_MakeMusic", 0, 142,
                false, "running_night",
                R.drawable.img_album_exp, false, 2
            )
        )
        songDB?.songDao()?.insert(
            Song(
                "Soft Background Music", "original_soundtrack", 0, 213,
                false, "soft_background_music",
                R.drawable.img_album_exp, false, 2
            )
        )
    }
}

