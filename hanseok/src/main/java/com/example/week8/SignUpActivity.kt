package com.example.week8

import UserRepository
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.week8.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener {
            // ▼▼▼ 3. 불필요한 finish() 제거 ▼▼▼
            signUp()
        }
    }

    private fun getUser(): User {
        val name = binding.signUpNameEt.text.toString()
        // ▼▼▼ 1. 이메일 생성 시 '@' 추가 ▼▼▼
        val email = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val password = binding.signUpPasswordEt.text.toString()

        // ▼▼▼ 2. name 필드를 포함하여 User 객체 생성 ▼▼▼
        return User("", email, password, name)
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

        val user = getUser()

        // UserRepository를 통해 회원가입 요청
        userRepository.signUp(user) { isSuccess, message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            // ▼▼▼ 3. 회원가입 성공 시에만 finish() 호출 ▼▼▼
            if (isSuccess) {
                finish() // 회원가입 화면 종료
            }
        }
    }
}