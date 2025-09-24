package com.example.week2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.week2.databinding.FragmentAlbumBinding

class AlbumFragment: Fragment() {
    lateinit var binding : FragmentAlbumBinding

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
            (context as Week2).supportFragmentManager.beginTransaction().replace(R.id.main_frm,
                HomeFragment()).commitAllowingStateLoss()
        }
//        binding.songLalacLayout.setOnClickListener{
//            Toast.makeText(activity,"LILAC", Toast.LENGTH_SHORT).show()
//        }
        return binding.root
    }
}