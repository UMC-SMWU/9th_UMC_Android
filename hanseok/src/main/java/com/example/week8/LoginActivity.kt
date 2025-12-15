package com.example.week8

import UserRepository
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.week8.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 회원가입 버튼 클릭 시 SignUpActivity로 이동
        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // 로그인 버튼 클릭 시
        binding.loginSignInBtn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val emailId = binding.loginIdEt.text.toString().trim()
        val emailDomain = binding.loginDirectInputEt.text.toString().trim() // 도메인 입력칸 ID
        val password = binding.loginPasswordEt.text.toString()

        // 2. 유효성 검사
        if (emailId.isEmpty() || emailDomain.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email = "$emailId@$emailDomain"

        userRepository.login(email, password) { isSuccess, message, user ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            if (isSuccess) {
                // 5. 로그인 성공 시
                saveUserId(user)

                // 6. 메인 화면(Week8)으로 이동
                val intent = Intent(this, Week8::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                // 로그인 실패 시
                Log.d("LOGIN", "Login failed: $message")
            }
        }
    }

    private fun saveUserId(user: User?) {
        if (user == null) return

        // "auth"라는 이름의 SharedPreferences 파일을 가져옵니다.
        val sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // "userId"라는 키로 사용자의 고유 ID(uid)를 저장합니다.
        editor.putString("userId", user.Id)
        editor.apply()
    }
}
