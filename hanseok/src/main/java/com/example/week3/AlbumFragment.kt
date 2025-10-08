package com.example.week3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.week3.HomeFragment
import com.example.week3.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment: Fragment() {
    lateinit var binding : FragmentAlbumBinding
    private val information = arrayListOf("수록곡", "상세정보", "영상")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        val title = arguments?.getString("title") ?: ""
        val singer = arguments?.getString("singer") ?: ""
        binding.albumMusicTitleTv.text = title
        binding.albumSingerNameTv.text = singer
        Log.d("AlbumFragment", "received title=$title, singer=$singer")

        binding.albumBackIv.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main_frm,
                HomeFragment()
            ).commitAllowingStateLoss()
        }
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("singer", singer)
        val albumAdapter = AlbumVPAdapter(this, bundle)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
            tab, position ->
            tab.text = information[position]
        }.attach()
        return binding.root
    }
}