package com.example.week3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.week3.databinding.FragmentHomeBannerBinding

class HomeBannerFragment(val imgRes : Int) : Fragment() {

    lateinit var binding : FragmentHomeBannerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBannerBinding.inflate(inflater, container, false)

        binding.homeBannerImageIv.setImageResource(imgRes)
        return binding.root
    }
}