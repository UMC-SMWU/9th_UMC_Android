package com.example.week7

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week7.databinding.FragmentStorageAlbumBinding
import com.example.week7.databinding.FragmentStorageSongBinding

class StorageSongFragment : Fragment() {

    private lateinit var binding: FragmentStorageSongBinding
    lateinit var songDB: SongDatabase
    private lateinit var songRVAdapter: StorageSongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStorageSongBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }


    override fun onStart(){
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        songRVAdapter = StorageSongRVAdapter()
        binding.storageSongRv.adapter = songRVAdapter
        binding.storageSongRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        songRVAdapter.setMyItemClickListener(object : StorageSongRVAdapter.MyItemClickListener {
            override fun onItemClick(song: Song) {
            }
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeByID(false, songId)
            }
        })

        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as kotlin.collections.ArrayList)
    }
}
