package com.example.week9_10.ui.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.week9_10.ui.main.MainActivity
import com.example.week9_10.ui.signup.SignupActivity
import com.example.week9_10.data.entities.User
import com.example.week9_10.data.remote.AuthService
import com.example.week9_10.data.remote.Data
import com.example.week9_10.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import retrofit2.http.Tag

class LoginActivity : AppCompatActivity(), LoginView {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java)) }
        binding.loginSignInBtn.setOnClickListener { login() }
        binding.loginKakakoLoginIv.setOnClickListener { kakaoLogin() }
    }
    private fun login(){
        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.loginPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val email = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val pwd = binding.loginPasswordEt.text.toString()

        val authService = AuthService()
        authService.setLoginView(this)
        authService.login(User(email = email, password = pwd))

        val spf = getSharedPreferences("auth2", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putString("email", email)
        editor.putString("password", pwd)
        editor.apply()
    }

    private fun saveJwt2(jwt: Int, token:String){
        val spf = getSharedPreferences("auth2", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putInt("jwt", jwt)
        editor.putString("token",token)
        editor.apply()
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun kakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부되었습니다 (동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "기타 에러가 발생했습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                Log.d("KakaoLogin", "카카오 로그인 성공")
                UserApiClient.instance.me{
                    user, error ->
                    if (error != null) {
                        Log.e("me/Error", "사용자 정보 요청 실패", error)
                    }
                    else if (user != null) {

                        val spf = getSharedPreferences("auth2", MODE_PRIVATE)
                        val editor = spf.edit()
                        editor.putInt("jwt", user.id?.toInt() ?: 0)
                        editor.putString("token", token.accessToken)
                        editor.putBoolean("kakao", true)
                        editor.putString("email", user.kakaoAccount?.email)
                        editor.putString("nickname", user.kakaoAccount?.profile?.nickname)
                        editor.putString("thumbnail", user.kakaoAccount?.profile?.thumbnailImageUrl)
                        editor.apply()
                        startMainActivity()
                    }
                }

            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
        } else {
            // TODO 카카오 계정으로 로그인 안 됨(브라우저 창이 안 띄워짐)
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    override fun onLoginSuccess(status:Boolean, data: Data) {
        when(status){
            true -> {
                saveJwt2(data.memberId, data.accessToken)   // 주로 유저 인덱스(memberId)와 jwt(accessToken) 전부 저장
                startMainActivity()
            }
            false -> {
                val spf = getSharedPreferences("auth2", MODE_PRIVATE)
                val editor = spf.edit()
                editor.remove("email")
                editor.remove("password")
                editor.apply()
                Toast.makeText(this, "Status is false", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLoginFailure() {
        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
    }
}