package com.example.yoonseo

import BrowseFragment
import HomeFragment
import LibraryFragment
import SearchFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.yoonseo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

        binding.mainPlayerCl.setOnClickListener {
            TODO()
            startActivity(intent)
        }

        // 엣지-투=엣지 킴
        WindowCompat.setDecorFitsSystemWindows(window, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // 루트에 안전패딩을 주면 하위 자식들이 자연스럽게 안전영역 안에 배치됨
            v.setPadding(v.paddingLeft, sysBars.top, v.paddingRight, sysBars.bottom)

            // 혹은 바텀 내비만 따로 처리하고 싶으면:
            // binding.mainBnv.updatePadding(bottom = sysBars.bottom)

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