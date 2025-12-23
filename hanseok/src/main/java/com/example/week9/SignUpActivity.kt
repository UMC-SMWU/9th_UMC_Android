package com.example.week9

import UserRepository
import android.R.id.message
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.week9.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity(), SignUpView {
    lateinit var binding: ActivitySignupBinding
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener {
            signUp()
        }
    }

    private fun getUser(): User {
        val name = binding.signUpNameEt.text.toString()
        val email = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val password = binding.signUpPasswordEt.text.toString()
        return User(email, password, name)
    }

    private fun signUp() {
        val name = binding.signUpNameEt.text.toString()
        val emailId = binding.signUpIdEt.text.toString()
        val emailDomain = binding.signUpDirectInputEt.text.toString()
        val password = binding.signUpPasswordEt.text.toString()
        val passwordCheck = binding.signUpPasswordCheckEt.text.toString()

        // 유효성 검사
        if (name.isEmpty() || emailId.isEmpty() || emailDomain.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != passwordCheck) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }


        val authService = AuthService()
        authService.setSignUpView(this)

        authService.signUp(getUser())
    }

    override fun onSignUpSuccess() {
        binding.signUpLoadingPb.visibility = View.GONE
        Toast.makeText(this, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSignUpFailure(code: String, message: String) {
        binding.signUpLoadingPb.visibility = View.GONE
        Toast.makeText(this, "회원가입에 실패했습니다.", Toast.LENGTH_LONG).show()
        finish()
    }

}