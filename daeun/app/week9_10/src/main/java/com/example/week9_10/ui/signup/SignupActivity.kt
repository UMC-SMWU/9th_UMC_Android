package com.example.week9_10.ui.signup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.week9_10.data.entities.User
import com.example.week9_10.data.remote.AuthService
import com.example.week9_10.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity(), SignUpView {
    lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signUpSignUpBtn.setOnClickListener { signUp(); finish() }
    }

    private fun getUser(): User {
        val email = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val pwd = binding.signUpPasswordEt.text.toString()
        val name: String = binding.signUpNameEt.text.toString()

        // TODO id 0으로 지정
        return User(0, name, email, pwd)
    }

    private fun signUp(){

        if(binding.signUpIdEt.text.toString().isEmpty() || binding.signUpDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일 형식이 잘못 되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signUpNameEt.text.toString().isEmpty()){
            Toast.makeText(this, "이름이 형식이 잘못 되었습니다", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()){
            Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setSignUpView(this)
        authService.signUp(getUser())
    }

    override fun onSignUpSuccess() {
        finish()
    }

    override fun onSignUpFailure() {
        Toast.makeText(this,"Sign up Failed",Toast.LENGTH_SHORT).show()
    }
}