package com.example.week5

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.week5.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


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
        return binding.root
    }
}