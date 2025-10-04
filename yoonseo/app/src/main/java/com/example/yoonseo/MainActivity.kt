package com.example.yoonseo

import BrowseFragment
import HomeFragment
import LibraryFragment
import SearchFragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import com.example.yoonseo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("title", binding.mainMiniplayerTitleTv.text.toString())
                putExtra("singer", binding.mainMiniplayerSingerTv.text.toString())
                putExtra("albumResId", R.drawable.img_album_exp2)
            }
            startActivity(intent)
        }

        // 엣지-투=엣지 킴
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = false
        controller.isAppearanceLightNavigationBars = false

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val status = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val nav = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            val frag = supportFragmentManager.findFragmentById(R.id.main_frm)
            val homeRoot = frag?.view
            val actions = homeRoot?.findViewById<View>(R.id.home_hero_actions_ll)
            actions?.updatePadding(top = status.top)

            // 루트에 안전패딩을 주면 하위 자식들이 자연스럽게 안전영역 안에 배치됨
            // v.setPadding(v.paddingLeft, sysBars.top, v.paddingRight, sysBars.bottom)

            insets
        }

    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.browseFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, BrowseFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.libraryFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LibraryFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}