package com.example.week7

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.week7.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class MainActivity() : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var song:Song? = null
    private var gson = Gson()
    lateinit var dataLoader: SongDataLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        setTheme(R.style.Theme_UMC_Study)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startDataLoad()

        binding.mainPlayerCl.setOnClickListener{
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song!!.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        initBottomNavigation()
    }

    private fun startDataLoad(){
        dataLoader = SongDataLoader(context = this)
        dataLoader.start()
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
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        //val songDB = SongDatabase.getInstance(this)
        val albumDB = AlbumDatabase.getInstance(this)
        song = if(songId == 0){
            //songDB?.songDao()?.getSong(1)
            albumDB?.songDao()?.getSong(1)
        }else{
            //songDB?.songDao()?.getSong(songId)
            albumDB?.songDao()?.getSong(songId)
        }
        Log.d("Song ID", song?.id.toString())
        setMiniPlayer(song!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        dataLoader.interrupt()
    }

    inner class SongDataLoader(private val context: Context): Thread(){
        override fun run() {
            super.run()
            //val songDB = SongDatabase.getInstance(context)
            val albumDB = AlbumDatabase.getInstance(context)
            //val songs = songDB?.songDao()?.getSongs()
            val songs = albumDB?.songDao()?.getSongs()
            try {

                if (songs?.isEmpty() == true) {
                    albumDB.albumDao().insert(
                        Album(
                            "Pixabay Song without Royalty",
                            "Many Singers",
                            R.drawable.img_album_exp,
                            "Songs from Pixabay",
                            false
                        )
                    )
                    albumDB?.songDao()?.insert(
                        Song(
                            "Background Music", "DELOSound", 0, 142,
                            false, "background_music",
                            R.drawable.img_album_exp, false, 1
                        )
                    )
                    albumDB?.songDao()?.insert(
                        Song(
                            "Christmas Holiday Festive Magic", "Top-Flow", 0, 79,
                            false, "christmas_holiday_festive_magic",
                            R.drawable.img_album_exp, false, 1
                        )
                    )
                    albumDB?.songDao()?.insert(
                        Song(
                            "Lofi Background Music", "DELOSound", 0, 92,
                            false, "lofi_background_music",
                            R.drawable.img_album_exp, false, 1
                        )
                    )
                    albumDB?.songDao()?.insert(
                        Song(
                            "Running Night", "Alex_MakeMusic", 0, 142,
                            false, "running_night",
                            R.drawable.img_album_exp, false, 1
                        )
                    )
                    albumDB?.songDao()?.insert(
                        Song(
                            "Soft Background Music", "original_soundtrack", 0, 213,
                            false, "soft_background_music",
                            R.drawable.img_album_exp, false, 1
                        )
                    )
                }
            } catch (e: InterruptedException) {
                Log.d("Load Song", "The thread is dead ${e.message}")
            }
        }
    }

}

