package com.example.week9

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week9.databinding.FragmentStorageAlbumBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StorageAlbumFragment : Fragment() {

    private lateinit var binding: FragmentStorageAlbumBinding
    private lateinit var albumRVAdapter: StorageAlbumRVAdapter
    private val albumsRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("albums")
    private var albumList = ArrayList<Album>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStorageAlbumBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart(){
        super.onStart()
        initRecyclerview()
        loadAlbumsFromFirebase()
    }

    private fun initRecyclerview(){
        albumRVAdapter = StorageAlbumRVAdapter()
        binding.storageAlbumRv.adapter = albumRVAdapter
        binding.storageAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        albumRVAdapter.setMyItemClickListener(object : StorageAlbumRVAdapter.MyItemClickListener {
            override fun onRemoveAlbum(albumId: String) {
                val position = albumList.indexOfFirst { it.id == albumId }
                if (position != -1) {
                    // 찾은 위치(position)를 사용하여 어댑터에서 아이템을 제거합니다.
                    albumRVAdapter.removeAlbum(position)
                    Toast.makeText(requireContext(), "화면에서만 앨범을 제거했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun loadAlbumsFromFirebase() {
        albumsRef.get().addOnSuccessListener { dataSnapshot ->
            albumList.clear()
            for (albumSnapshot in dataSnapshot.children) {
                val album = albumSnapshot.getValue(Album::class.java)
                album?.let {
                    albumList.add(it)
                }
            }
            albumRVAdapter.addAlbums(albumList)
            Log.d("StorageAlbumFragment", "Firebase에서 앨범 로드 성공: ${albumList.size}개")

        }.addOnFailureListener {
            Log.e("StorageAlbumFragment", "Firebase 앨범 로드 실패", it)
        }
    }

}
