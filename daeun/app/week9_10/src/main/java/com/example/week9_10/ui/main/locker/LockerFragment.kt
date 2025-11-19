package com.example.week9_10.ui.main.locker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.week9_10.ui.main.MainActivity
import com.example.week9_10.databinding.FragmentLockerBinding
import com.example.week9_10.ui.mypage.MypageActivity
import com.example.week9_10.ui.signin.LoginActivity
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf("저장한 곡", "음악파일", "저장앨범")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        val adapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = adapter
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()
        binding.lockerLoginTv.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    private fun getJwt(): Int{
        val spf = activity?.getSharedPreferences("auth2", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }
    private fun initView(){
        val jwt = getJwt()
        if(jwt == 0){
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        } else{
            binding.lockerLoginTv.text = "마이페이지"
            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity, MypageActivity::class.java))
            }
        }
    }
}