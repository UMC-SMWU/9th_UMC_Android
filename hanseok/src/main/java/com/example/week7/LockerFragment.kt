package com.example.week7

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.week7.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.jvm.java


class LockerFragment() : Fragment() {
    private val information = arrayListOf("저장한 곡", "음악파일", "저장앨범")
    lateinit var binding : FragmentLockerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockercontentAdapter = LockerContentVPAdapter(this)
        binding.lockerContentVp.adapter = lockercontentAdapter
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp){
                tab, position ->
            tab.text = information[position]
        }.attach()

        binding.lockerLoginTv.setOnClickListener {
            val intent = Intent(requireContext(), SignUpActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}