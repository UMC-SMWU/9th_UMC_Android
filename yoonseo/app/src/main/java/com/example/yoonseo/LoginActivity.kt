package com.example.yoonseo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.yoonseo.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userDB: UserDatabase
    // private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDB = UserDatabase.getInstance(this)!!
        // Firebase Auth 초기화
        // auth = Firebase.auth

        // 닫기 버튼
        binding.loginCloseIv.setOnClickListener {
            finish()
        }

        // 회원가입 버튼
        binding.loginSignUpTv.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼
        binding.loginBtn.setOnClickListener {
            login()
        }

        // 비밀번호 보이기/숨기기
        setupPasswordVisibility()
    }

    private fun setupPasswordVisibility() {
        var isPasswordVisible = false
        binding.loginHidePasswordIv.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.loginPasswordEt.inputType =
                    android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.loginHidePasswordIv.setImageResource(R.drawable.btn_input_password)
            } else {
                binding.loginPasswordEt.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.loginHidePasswordIv.setImageResource(R.drawable.btn_input_password_off)
            }
            // cursor 끝으로 이동
            binding.loginPasswordEt.setSelection(binding.loginPasswordEt.text.length)
        }
    }

    private fun login() {
        val email = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val password = binding.loginPasswordEt.text.toString()

        // 입력값 검증하기
        if(binding.loginIdEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.loginDirectInputEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 도메인을 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }
        if(password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }

        // DB에서 사용자 확인
        val user = userDB.userDao().getUser(email, password)
        if(user != null) {
            // 로그인 성공
            Log.d("LoginActivity", "로그인 성공: ${user.email}")
            // SharePreferences에 사용자 정보 저장
            val sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("userId", user.id)
            editor.putString("userEmail", user.email)
            editor.putBoolean("isLoggedIn", true)
            // 비동기 저장
            editor.apply()

            Toast.makeText(this, "로그인 되었습니다", Toast.LENGTH_SHORT).show()

            // MainActivity로 돌아가기
            finish()
        } else {
            // 로그인 실패
            Toast.makeText(this, "이메일 또는 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
        }

    }
}