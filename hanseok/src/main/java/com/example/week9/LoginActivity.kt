package com.example.week9

import UserRepository
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.week9.SignUpActivity
import com.example.week9.User
import com.example.week9.databinding.ActivityLoginBinding
import com.example.week9.Week9
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause

import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity(), LoginView, TestView {

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
        binding.tokenTestBtn.setOnClickListener {
            testToken()
        }
        binding.loginKakakoLoginIv.setOnClickListener {
            kakaoLogin()
        }
        binding.loginKakakoLogoutIv.setOnClickListener {
            kakaoLogout()
        }
    }
    private fun kakaoLogout() {
        // 로그아웃버튼 직접구현
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("KakaoLogout", "로그아웃 실패", error)
                Toast.makeText(this, "로그아웃 실패", Toast.LENGTH_SHORT).show()
            } else {
                Log.i("KakaoLogout", "로그아웃 성공")
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        // 로그아웃하면 사용자 정보가 없기 때문에 요청 실패가 나오게됨
                        Log.e("TAG", "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        Log.i("TAG", "=== 카카오 사용자 정보 ===")
                        Log.i("TAG", "ID: ${user.id}")
                        Log.i("TAG", "nickname: ${user.kakaoAccount?.profile?.nickname}")
                        Log.i("TAG", "profileImage: ${user.kakaoAccount?.profile?.profileImageUrl}")
                        Log.i("TAG", "======================")
                        Toast.makeText(this, "로그아웃 성공!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun kakaoLogin() {
        UserApiClient.instance.logout { _ ->
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Toast.makeText(this, "로그인 실패: ${error.message}", Toast.LENGTH_SHORT).show()
                } else if (token != null) {
                    Log.d("KakaoLogin", "카카오 로그인 성공: ${token.accessToken}")

                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e("TAG", "사용자 정보 요청 실패", error)
                        } else if (user != null) {
                            Log.i("TAG", "=== 카카오 사용자 정보 ===")
                            Log.i("TAG", "ID: ${user.id}")
                            Log.i("TAG", "nickname: ${user.kakaoAccount?.profile?.nickname}")
                            Log.i("TAG", "profileImage: ${user.kakaoAccount?.profile?.profileImageUrl}")
                            Log.i("TAG", "======================")


                            Toast.makeText(this, "카카오 로그인 완료!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, Week9::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    private fun testToken() {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val accessToken = spf.getString("accessToken", null)

        if (accessToken == null) {
            Toast.makeText(this, "토큰이 없습니다. 로그인을 해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        val authService = AuthService()
        authService.setTestView(this)
        authService.testApi(accessToken)
    }

    private fun login() {
        val emailId = binding.loginIdEt.text.toString().trim()
        val emailDomain = binding.loginDirectInputEt.text.toString().trim() // 도메인 입력칸 ID
        val password = binding.loginPasswordEt.text.toString()

        if (emailId.isEmpty() || emailDomain.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email = "$emailId@$emailDomain"

//        userRepository.login(email, password) { isSuccess, message, user ->
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//
//            if (isSuccess) {
//                saveUserId(user)
//
//                val intent = Intent(this, Week9::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intent)
//            } else {
//                Log.d("LOGIN", "Login failed: $message")
//            }
//        }

        val user = User(email, password, "")

        val authService = AuthService()
        authService.setLoginView(this)
        authService.login(user)
    }

    private fun saveJwt(accessToken: String) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putString("accessToken", accessToken)
        editor.apply()
    }

    private fun saveUserId(user: User?) {
        if (user == null) return

        val sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("email", user.email)
        editor.apply()
    }

    override fun onLoginSuccess(result: AuthResult) {
        Toast.makeText(this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()

        // 로그인 성공 시 받은 accessToken을 SharedPreferences에 저장
        if (result.accessToken != null) {
            saveJwt(result.accessToken)
        }

        // 메인 화면(Week9)으로 이동하고, 이전 액티비티 스택을 모두 제거
        val intent = Intent(this, Week9::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


    override fun onLoginFailure(code: String, message: String) {
        Toast.makeText(this, "로그인 실패: $message (코드: $code)", Toast.LENGTH_LONG).show()
    }

    override fun onTestSuccess(message: String) {
        Toast.makeText(this, "API 테스트 성공: $message", Toast.LENGTH_SHORT).show()
        binding.tokenTestTv.text = message
    }

    override fun onTestFailure(code: Int, message: String) {
        Toast.makeText(this, "API 테스트 실패: $message (코드: $code)", Toast.LENGTH_LONG).show()
    }
}
