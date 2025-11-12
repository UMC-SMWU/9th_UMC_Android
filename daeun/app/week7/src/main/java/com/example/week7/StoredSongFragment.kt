package com.example.week7

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week7.databinding.FragmentStoredSongBinding
//import com.google.firebase.database.FirebaseDatabase


class StoredSongFragment: Fragment() {
    lateinit var binding: FragmentStoredSongBinding
    lateinit var albumDB: AlbumDatabase
    private var songData = ArrayList<Song>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoredSongBinding.inflate(inflater, container, false)

        val storedSongRVAdapter = StoredSongRVAdapter(requireContext(), songData)
        binding.storedSongContentRv.adapter = storedSongRVAdapter
        binding.storedSongContentRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)

        albumDB = AlbumDatabase.getInstance(requireContext())!!
        songData = albumDB.songDao().getLikeSongs(true) as ArrayList<Song>
        //val songRef = FirebaseDatabase.getInstance().getReferences("songs")
        storedSongRVAdapter.addSongs(songData)

        storedSongRVAdapter.setMyItemClickListener(object: StoredSongRVAdapter.MyItemClickListener{
            override fun onRemoveAlbum(position: Int) {
                storedSongRVAdapter.removeItem(position)
            }
        })

        return binding.root
    }
}