package com.example.week7

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week7.databinding.FragmentStorageAlbumBinding

class StorageAlbumFragment : Fragment() {

    private lateinit var binding: FragmentStorageAlbumBinding
    lateinit var songDB: SongDatabase
    private lateinit var albumRVAdapter: StorageAlbumRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStorageAlbumBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart(){
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        albumRVAdapter = StorageAlbumRVAdapter()
        binding.storageAlbumRv.adapter = albumRVAdapter
        binding.storageAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        albumRVAdapter.setMyItemClickListener(object : StorageAlbumRVAdapter.MyItemClickListener {
            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeSong(position)
            }
        })

        albumRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList)
    }
}
