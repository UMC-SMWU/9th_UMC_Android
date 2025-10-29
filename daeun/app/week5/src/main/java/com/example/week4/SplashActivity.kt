package com.example.week4

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.week4.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity(){
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val handler: Handler(Looper.getMainLooper())
        //handler.postDelayed({
        //    startActivity(Intent(this, MainActivity::class.java))
        //}, 1000)
    }
}


