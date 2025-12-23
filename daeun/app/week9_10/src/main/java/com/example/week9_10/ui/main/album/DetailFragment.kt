package com.example.week9_10.ui.main.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week9_10.databinding.FragmentDetailBinding

class DetailFragment: Fragment() {
    lateinit var binding: FragmentDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val songTitle = arguments?.getString("songTitle")
        val singer = arguments?.getString("singer")
        binding.detailSongTitleTv.text = songTitle
        binding.detailComposerTv.text = singer
        return binding.root
    }
}