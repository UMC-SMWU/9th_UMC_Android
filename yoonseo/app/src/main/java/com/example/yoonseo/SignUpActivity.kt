package com.example.yoonseo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.yoonseo.databinding.ActivitySignUpBinding
import java.nio.file.Files.find

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var userDB: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDB = UserDatabase.getInstance(this)!!

        // 닫기 버튼
        binding.signUpCloseIv.setOnClickListener {
            finish()
        }

        // 가입완료 버튼
        binding.loginBtn.setOnClickListener {
            signUp()
        }

        // 첫 번째 비밀번호 보이기/숨기기
        var isPasswordVisible = false
        binding.signUpHidePasswordIv.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.signUpPasswordEt.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.signUpHidePasswordIv.setImageResource(R.drawable.btn_input_password)
            } else {
                binding.signUpPasswordEt.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.signUpHidePasswordIv.setImageResource(R.drawable.btn_input_password_off)
            }
            binding.signUpPasswordEt.setSelection(binding.signUpPasswordEt.text.length)
        }
        // 두 번째 비밀번호 보이기/숨기기
        var isPasswordTwiceVisible = false
        binding.signUpPasswordTwiceHidePasswordIv.setOnClickListener {
            isPasswordTwiceVisible = !isPasswordTwiceVisible
            if (isPasswordTwiceVisible) {
                binding.signUpPasswordTwiceEt.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.signUpPasswordTwiceHidePasswordIv.setImageResource(R.drawable.btn_input_password)
            } else {
                binding.signUpPasswordTwiceEt.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.signUpPasswordTwiceHidePasswordIv.setImageResource(R.drawable.btn_input_password_off)
            }
            binding.signUpPasswordTwiceEt.setSelection(binding.signUpPasswordTwiceEt.text.length)
        }
    }

    private fun signUp() {
        val email = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val password = binding.signUpPasswordEt.text.toString()
        val passwordTwice = binding.signUpPasswordTwiceEt.text.toString()

        /** 입력값 검증 **/
        if (binding.signUpIdEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.signUpDirectInputEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 도메인을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (passwordTwice.isEmpty()) {
            Toast.makeText(this, "비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != passwordTwice) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val existingUser = userDB.userDao().getUsers().find { it.email == email }
        if (existingUser != null) {
            Toast.makeText(this, "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // 회원가입 처리
        val user = User(email = email, password = password)
        userDB.userDao().insert(user)

        Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
        // 로그인 화면으로 돌아가기
        finish()
    }
}