package com.example.yoonseo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.yoonseo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

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
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateLoginStatus()
    }

    private fun updateLoginStatus() {
        val sharedPreferences = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if(isLoggedIn) {
            val userEmail = sharedPreferences.getString("userEmail", "")
            binding.lockerLoginTv.text = userEmail?.substringBefore("@") ?: "사용자"
        } else {
            binding.lockerLoginTv.text = "로그인"
        }
    }

}