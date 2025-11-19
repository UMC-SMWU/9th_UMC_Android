package com.example.week8

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week8.databinding.FragmentLookBinding

class LookFragment : Fragment() {

    lateinit var binding: FragmentLookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLookBinding.inflate(inflater, container, false)
        val themeList = arrayListOf("차트", "영상", "장르", "상황", "분위기", "분위기2")
        val adapter = LookRVAdapter(themeList)
        binding.lookThemeRv.adapter = adapter
        return binding.root
    }
}