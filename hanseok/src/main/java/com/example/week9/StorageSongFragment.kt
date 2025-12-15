package com.example.week9

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week9.databinding.FragmentStorageSongBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StorageSongFragment : Fragment() {

    private lateinit var binding: FragmentStorageSongBinding
    private lateinit var songRVAdapter: StorageSongRVAdapter
    private val database = FirebaseDatabase.getInstance()
    private val songsRef = database.getReference("songs")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStorageSongBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        initRecyclerView()
        loadLikedSongsFromFirebase()
    }

    private fun initRecyclerView() {
        songRVAdapter = StorageSongRVAdapter()
        binding.storageSongRv.adapter = songRVAdapter
        binding.storageSongRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        songRVAdapter.setMyItemClickListener(object : StorageSongRVAdapter.MyItemClickListener {
            override fun onItemClick(song: Song) {
            }

            override fun onRemoveSong(songId: String) {
                songsRef.child(songId).child("isLike").setValue(false)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "좋아요 목록에서 삭제했습니다.", Toast.LENGTH_SHORT).show()
                    }
            }
        })
    }

    private fun loadLikedSongsFromFirebase() {
        songsRef.orderByChild("isLike").equalTo(true).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val likedSongs = ArrayList<Song>()
                for (songSnapshot in snapshot.children) {
                    val song = songSnapshot.getValue(Song::class.java)
                    song?.let {
                        likedSongs.add(it)
                    }
                }
                Log.d("StorageSongFragment", "좋아요 한 곡 실시간 로드: ${likedSongs.size}개")
                songRVAdapter.addSongs(likedSongs)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("StorageSongFragment", "좋아요 한 곡 로드 실패", error.toException())
            }
        })
    }
}
