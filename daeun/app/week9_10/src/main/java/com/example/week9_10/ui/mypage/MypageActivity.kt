package com.example.week9_10.ui.mypage

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.week9_10.data.entities.User
import com.example.week9_10.data.entities.UserUpdate
import com.example.week9_10.data.remote.AuthService
import com.example.week9_10.databinding.ActivityMypageBinding
import com.kakao.sdk.user.UserApiClient
import java.net.URI

class MypageActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMypageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authService = AuthService()
        val spf = getSharedPreferences("auth2",MODE_PRIVATE)
        val jwt = spf.getInt("jwt",0)
        val token = spf.getString("token","") ?: ""
        val pwd = spf.getString("password", "")?: ""
        val kakao = spf.getBoolean("kakao", false)
        val email = spf.getString("email", "week910@example.com")
        val nickname = spf.getString("nickname", "")
        val thumbnail = spf.getString("thumbnail", "")

        binding.myPageBackArrowIb.setOnClickListener { finish() }

        if(kakao){
            binding.kakaoLl.visibility = View.VISIBLE
            binding.notKakaoLl.visibility = View.GONE

            binding.kakaoProfileIv.setImageURI(Uri.parse(thumbnail))
            binding.kakaoEmailTv.text = email
            binding.kakaoNicknameTv.text = nickname
            binding.kakaoLogoutBtn.setOnClickListener { kakaoLogOut(); finish() }

        }else{
            binding.kakaoLl.visibility = View.GONE
            binding.notKakaoLl.visibility = View.VISIBLE

            var changeInfoBtnClicked = false

            binding.tokenTestBtn.setOnClickListener { authService.test(token) }
            binding.changeInfoBtn.setOnClickListener {
                if(changeInfoBtnClicked){
                    binding.myPageChangeInfoCl.visibility = View.GONE
                    changeInfoBtnClicked = false
                }

                else{
                    binding.myPageChangeInfoCl.visibility = View.VISIBLE
                    changeInfoBtnClicked = true
                }
            }
            binding.myPageConfirmBtn.setOnClickListener {
                val newName = binding.myPageNameEt.text.toString()
                val newPwd = binding.myPagePasswordEt.text.toString()
                authService.update(token, UserUpdate(jwt,newName, newPwd))
                Toast.makeText(this, "Successfully Changed your Information", Toast.LENGTH_SHORT).show()
            }
            binding.logoutBtn.setOnClickListener { logOut(); finish() }
            binding.unsubscribeBtn.setOnClickListener{
                authService.delete(token, jwt,pwd)
                logOut()
                finish()
            }
        }

    }
    private fun logOut(){
        val spf = this.getSharedPreferences("auth2",MODE_PRIVATE)
        val editor = spf?.edit()
        editor?.remove("jwt")
        editor?.remove("token")
        editor?.remove("email")
        editor?.remove("password")
        editor?.apply()
    }

    private fun kakaoLogOut(){
        UserApiClient.instance.logout {
            error ->
            if(error != null)
                Log.i("LogOut/Error", "로그아웃 실패. SDK에서 토큰 폐기됨", error)
            else{
                Log.i("LogOut/Success", "로그아웃 성공. SDK에서 토큰 폐기됨")
                val spf = this.getSharedPreferences("auth2",MODE_PRIVATE)
                val editor = spf?.edit()
                editor?.remove("jwt")
                editor?.remove("token")
                editor?.remove("kakao")
                editor?.remove("email")
                editor?.remove("nickname")
                editor?.remove("thumbnail")
                editor?.apply()
            }
        }
    }
}