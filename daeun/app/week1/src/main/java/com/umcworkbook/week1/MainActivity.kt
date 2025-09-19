package com.umcworkbook.week1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.umcworkbook.week1.R.color.white
import com.umcworkbook.week1.databinding.ActivityMainBinding
import com.umcworkbook.week1.ui.theme.UMC_StudyTheme

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.happyIcon.setOnClickListener {
            binding.happyText.setBackgroundColor(getColor(R.color.yellow))
            binding.excitedText.setBackgroundColor(getColor(white))
            binding.normalText.setBackgroundColor(getColor(white))
            binding.anxiousText.setBackgroundColor(getColor(white))
            binding.angryText.setBackgroundColor(getColor(white))
            Toast.makeText(this, "You feel happy today :)!", Toast.LENGTH_SHORT).show()
        }
        binding.excitedIcon.setOnClickListener {
            binding.happyText.setBackgroundColor(getColor(white))
            binding.excitedText.setBackgroundColor(getColor(R.color.sky_blue))
            binding.normalText.setBackgroundColor(getColor(white))
            binding.anxiousText.setBackgroundColor(getColor(white))
            binding.angryText.setBackgroundColor(getColor(white))
            Toast.makeText(this, "You feel excited today ;)!", Toast.LENGTH_SHORT).show()

        }
        binding.normalIcon.setOnClickListener {
            binding.happyText.setBackgroundColor(getColor(white))
            binding.excitedText.setBackgroundColor(getColor(white))
            binding.normalText.setBackgroundColor(getColor(R.color.blue))
            binding.anxiousText.setBackgroundColor(getColor(white))
            binding.angryText.setBackgroundColor(getColor(white))
            Toast.makeText(this, "You feel neutral today :|", Toast.LENGTH_SHORT).show()

        }
        binding.anxiousIcon.setOnClickListener {
            binding.happyText.setBackgroundColor(getColor(white))
            binding.excitedText.setBackgroundColor(getColor(white))
            binding.normalText.setBackgroundColor(getColor(white))
            binding.anxiousText.setBackgroundColor(getColor(R.color.green))
            binding.angryText.setBackgroundColor(getColor(white))
            Toast.makeText(this, "You feel anxious today (•᷄- •᷅ ;)", Toast.LENGTH_SHORT).show()

        }
        binding.angryIcon.setOnClickListener {
            binding.happyText.setBackgroundColor(getColor(white))
            binding.excitedText.setBackgroundColor(getColor(white))
            binding.normalText.setBackgroundColor(getColor(white))
            binding.anxiousText.setBackgroundColor(getColor(R.color.white))
            binding.angryText.setBackgroundColor(getColor(R.color.red))
            Toast.makeText(this, "You feel angry today >:|", Toast.LENGTH_SHORT).show()
        }

    }
}