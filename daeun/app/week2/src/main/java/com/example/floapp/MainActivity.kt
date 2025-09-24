package com.example.floapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.floapp.databinding.ActivityMainBinding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.floapp.ui.theme.UMC_StudyTheme

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    companion object{ const val STRING_INTENT_KEY = "songTitle"}
    private val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val returnString = result.data?.getStringExtra(STRING_INTENT_KEY)
            Toast.makeText(this@MainActivity,
                "Successfully received the song title: $returnString",
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.song_fragmentContainer, HomeFragment())
            .commit()

        // 시행 안 됨
        binding.mainBnv.setOnClickListener { item ->
            when(item.id){
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragmentContainer, HomeFragment())
                        .commit()
                    binding.mainDefaultView.visibility = View.VISIBLE
                }
                R.id.personal_storage -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragmentContainer, LockerFragment())
                        .commit()
                    binding.mainDefaultView.visibility = View.INVISIBLE
                }
            }
        }

        binding.miniPlayerPlay.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("songTitle", "defaultSongTitle")
            getResultText.launch(intent)
            //startActivity(intent)
        }
    }
}
