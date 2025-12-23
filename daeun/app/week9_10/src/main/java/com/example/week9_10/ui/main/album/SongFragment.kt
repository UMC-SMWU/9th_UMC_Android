package com.example.week9_10.ui.main.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week9_10.SongDatabase
import com.example.week9_10.data.entities.Song
import com.example.week9_10.databinding.FragmentSongBinding

class SongFragment: Fragment() {
    lateinit var binding: FragmentSongBinding
    private var songData = ArrayList<Song>()
    lateinit var songDB: SongDatabase
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)
        songDB = SongDatabase.Companion.getInstance(requireContext())!!
        songData = songDB.songDao().getSongs() as ArrayList

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