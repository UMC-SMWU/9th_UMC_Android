package com.example.week8

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week8.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    lateinit var binding : FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val title = arguments?.getString("title") ?: ""
        val singer = arguments?.getString("singer") ?: ""
        binding.detailTvTitle.text = "이 앨범의 이름은 " + title + "입니다."
        binding.detailTvSinger.text = "이 가수의 이름은 " + singer + "입니다."
        return binding.root
    }

}