package com.example.week4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week4.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    lateinit var binding : FragmentSongBinding
    private val songList = ArrayList<Song>()
    private lateinit var songRVAdapter: SongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)
        binding.songMixoffTg.setOnClickListener {
            binding.songMixoffTg.visibility = View.GONE
            binding.songMixonTg.visibility = View.VISIBLE

        }
        binding.songMixonTg.setOnClickListener {
            binding.songMixoffTg.visibility = View.VISIBLE
            binding.songMixonTg.visibility = View.GONE
        }

        songList.apply {
            add(Song(1, "Butter", "방탄소년단"))
            add(Song(2, "Lilac", "아이유"))
            add(Song(3, "Next Level", "에스파"))
            add(Song(4, "Boy with Luv", "방탄소년단"))
            add(Song(5, "BBoom BBoom", "모모랜드"))
            add(Song(6, "Weekend", "태연"))
        }

        songRVAdapter = SongRVAdapter(songList)
        binding.songAlbumRv.layoutManager = LinearLayoutManager(context)
        binding.songAlbumRv.adapter = songRVAdapter

        return binding.root
    }

}