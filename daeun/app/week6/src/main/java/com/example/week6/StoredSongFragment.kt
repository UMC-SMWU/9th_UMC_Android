package com.example.week6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week6.databinding.FragmentStoredSongBinding

class StoredSongFragment: Fragment() {
    lateinit var binding: FragmentStoredSongBinding
    private var songData = ArrayList<Song>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoredSongBinding.inflate(inflater, container, false)
        songData.apply{
            add(Song("LILAC", "아이유(IU)"))
            add(Song("Flu", "아이유(IU)"))
            add(Song("Coin", "아이유(IU)"))
            add(Song("봄 안녕 봄", "아이유(IU)"))
            add(Song("Celebrity", "아이유(IU)"))
        }
        val storedSongRVAdapter = StoredSongRVAdapter(songData)
        binding.storedSongContentRv.adapter = storedSongRVAdapter
        binding.storedSongContentRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)
        storedSongRVAdapter.setMyItemClickListener(object: StoredSongRVAdapter.MyItemClickListener{
            override fun onRemoveAlbum(position: Int) {
                storedSongRVAdapter.removeItem(position)
            }
        })
        return binding.root
    }
}