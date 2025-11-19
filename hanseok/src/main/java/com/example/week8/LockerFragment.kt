package com.example.week8

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.week8.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {
    private val information = arrayListOf("저장한 곡", "음악파일", "저장앨범")
    lateinit var binding : FragmentLockerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerContentAdapter = LockerContentVPAdapter(this)
        binding.lockerContentVp.adapter = lockerContentAdapter
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp){ tab, position ->
            tab.text = information[position]
        }.attach()

        // onStart에서 로그인 상태를 확인하고 UI를 설정하므로 onCreateView에서는 클릭 리스너를 설정하지 않습니다.
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // 프래그먼트가 화면에 보일 때마다 로그인 상태를 다시 확인하여 UI를 갱신합니다.
        initViews()
    }

    /**
     * SharedPreferences에서 저장된 userId(String)를 가져오는 함수.
     * 저장된 값이 없으면 null을 반환합니다.
     */
    private fun getUserId(): String? {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf?.getString("userId", null)
    }

    /**
     * 로그인 상태에 따라 UI(텍스트, 클릭 리스너)를 초기화하는 함수
     */
    private fun initViews(){
        val userId = getUserId()
        if(userId == null){
            // 로그아웃 상태
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        } else {
            // 로그인 상태
            binding.lockerLoginTv.text = "로그아웃"
            binding.lockerLoginTv.setOnClickListener {
                logout()
                // 로그아웃 후 메인 화면으로 이동하여 UI를 완전히 새로고침합니다.
                val intent = Intent(activity, Week8::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    /**
     * 로그아웃 처리 함수. SharedPreferences에서 userId를 제거합니다.
     */
    private fun logout(){
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()
        editor.remove("userId") // "jwt" 대신 "userId"를 제거합니다.
        editor.apply()
    }
}
