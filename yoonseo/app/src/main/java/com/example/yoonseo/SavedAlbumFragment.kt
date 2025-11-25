package com.example.yoonseo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yoonseo.databinding.FragmentLockerSavedalbumBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedAlbumFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedalbumBinding
    lateinit var songDB: SongDatabase
    private lateinit var albumRVAdapter: SavedAlbumRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedalbumBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        initRecyclerview()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadLikedAlbums()
    }

    override fun onStart() {
        super.onStart()
        loadLikedAlbums()
    }

    private fun initRecyclerview(){
        binding.lockerSavedAlbumRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        albumRVAdapter = SavedAlbumRVAdapter()
        // 삭제 클릭 리스너
        albumRVAdapter.setMyItemClickListener(object : SavedAlbumRVAdapter.MyItemClickListener{
            override fun onRemoveAlbum(albumId: Int) {
                removeAlbum(albumId)
            }
        })
        binding.lockerSavedAlbumRecyclerView.adapter = albumRVAdapter
    }

    private fun loadLikedAlbums() {
        val userId = getUserId()

        if(userId == 0) {
            Log.d("SavedAlbumFragment", "로그인되지 않음")
            albumRVAdapter.addAlbums(ArrayList())
            return
        }
        lifecycleScope.launch {
            val likedAlbums = withContext(Dispatchers.IO) {
                songDB.albumDao().getLikedAlbumsByUser(userId)
            }
            withContext(Dispatchers.Main) {
                Log.d("SavedAlbumFragment", "좋아요한 앨범 수: ${likedAlbums.size}")
                albumRVAdapter.addAlbums(likedAlbums as ArrayList<Album>)
            }
        }
    }

    private fun removeAlbum(albumId: Int) {
        val userId = getUserId()
        if(userId == 0) {
            Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                songDB.albumDao().updateIsLikeById(false, albumId)
                songDB.albumDao().disLikeAlbum(userId, albumId)
                Log.d("SavedAlbumFragment", "앨범 삭제: $albumId")
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "저장앨범에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                loadLikedAlbums()   // 리스트 새로 고침
            }
        }
    }

    private fun getUserId(): Int {
        val sharedPreferences = requireActivity().getSharedPreferences("auth", android.content.Context.MODE_PRIVATE)
        return sharedPreferences.getInt("userId", 0)
    }

}