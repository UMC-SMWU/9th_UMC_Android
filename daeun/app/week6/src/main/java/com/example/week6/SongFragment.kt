package com.example.week6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week6.databinding.FragmentDetailBinding
import com.example.week6.databinding.FragmentSongBinding

class SongFragment: Fragment() {
    lateinit var binding: FragmentSongBinding
    private var songData = ArrayList<Song>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)
        songData.apply{
            add(Song("LILAC", "아이유(IU)"))
            add(Song("Flu", "아이유(IU)"))
            add(Song("Coin", "아이유(IU)"))
            add(Song("봄 안녕 봄", "아이유(IU)"))
            add(Song("Celebrity", "아이유(IU)"))
        }
        val songRVAdapter = SongRVAdapter(songData)
        binding.songMusicListRv.adapter = songRVAdapter
        binding.songMusicListRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)
        songRVAdapter.setMyItemClickListener(object: SongRVAdapter.MyItemClickListener{
            override fun onRemoveAlbum(position: Int) {
                songRVAdapter.removeItem(position)
            }
        })
        return binding.root
    }
}