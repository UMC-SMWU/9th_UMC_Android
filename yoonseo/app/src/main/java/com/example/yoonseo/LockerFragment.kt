package com.example.yoonseo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.yoonseo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator
import androidx.core.content.edit

class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf("저장한곡", "음악파일", "저장앨범")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp){
                tab, position ->
            tab.text = information[position]
        }.attach()

        // 로그인 버튼 클릭 이벤트
        binding.lockerLoginTv.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        updateLoginStatus()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    private fun initViews() {
        val userId: Int = getUserId()
        if (userId == 0) {
            Log.d("LockerFragment", "로그인 필요")
        } else {
            Log.d("LockerFragment", "로그인됨: userId=$userId")
        }
    }

    override fun onResume() {
        super.onResume()
        updateLoginStatus()
    }

    private fun updateLoginStatus() {
        val sharedPreferences = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if(isLoggedIn) {
            // 로그인 된 상태
            val userEmail = sharedPreferences.getString("userEmail", "")
            binding.lockerLoginTv.text = userEmail?.substringBefore("@") ?: "사용자"
            // 로그인 된 상태에서 클릭 시 로그아웃
            binding.lockerLoginTv.setOnClickListener {
                logout()
            }
        } else {
            // 로그인 안된 상태
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun logout() {
        val sharedPreferences = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
        sharedPreferences.edit {
            clear()
            apply()
        }
        android.widget.Toast.makeText(requireContext(), "로그아웃 되었습니다.", android.widget.Toast.LENGTH_SHORT).show()
        updateLoginStatus()
    }

    private fun getUserId(): Int {
        val sharedPreferences = requireActivity().getSharedPreferences("auth", MODE_PRIVATE)
        return sharedPreferences.getInt("userId", 0)
    }
}