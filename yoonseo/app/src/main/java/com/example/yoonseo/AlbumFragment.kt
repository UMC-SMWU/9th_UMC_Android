package com.example.yoonseo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.yoonseo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private val information = arrayListOf("수록곡", "상세정보", "영상")
    private lateinit var songDB: SongDatabase
    private var currentAlbum: Album? = null
    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater,container,false)

        // 초기화
        songDB = SongDatabase.getInstance(requireContext())!!

        // 데이터 받아오기
        val albumData = arguments?.getString("album")
        val gson = Gson()
        currentAlbum = gson.fromJson(albumData, Album::class.java)

        currentAlbum?.let { album ->
            val userId = getUserId()

            lifecycleScope.launch {
                isLiked =  withContext(Dispatchers.IO) {
                    if (userId == 0) {
                        false
                    } else {
                        val likeId = songDB.albumDao().isLikedAlbum(userId, album.id)
                        likeId != null
                    }
                }
                withContext(Dispatchers.Main) {
                    setViews(album)
                    initViewPager()
                    setClickListeners(album)
                }
            }
        }
        return binding.root
    }

    private fun setViews(album: Album) {
        binding.albumMusicTitleTv.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()
        binding.albumAlbumIv.setImageResource(album.coverImg ?: R.drawable.fascination_moon)

        updateLikeButton()
    }

    private fun updateLikeButton() {
        if(isLiked) {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun setClickListeners(album: Album) {

        binding.albumLikeIv.setOnClickListener {
            toggleLike(album)
        }
        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }
    }

    private fun toggleLike(album: Album) {
        val userId = getUserId()

        if(userId == 0) {
            Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                if (isLiked) {
                    songDB.albumDao().updateIsLikeById(false, album.id)
                    songDB.albumDao().disLikeAlbum(userId, album.id)
                    Log.d("AlbumFragment", "앨범 좋아요 취소: ${album.title}")
                } else {
                    songDB.albumDao().updateIsLikeById(true, album.id)
                    val like = Like(userId = userId, albumId = album.id)
                    songDB.albumDao().likeAlbum(like)
                    Log.d("AlbumFragment", "앨범 좋아요: ${album.title}")
                }
            }

            withContext(Dispatchers.Main) {
                isLiked = !isLiked
                updateLikeButton()

                val message = if (isLiked) "저장앨범에 추가되었습니다." else "저장앨범에서 삭제되었습니다."
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initViewPager() {
        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()
    }

    private fun getUserId(): Int {
        val sharedPreferences = requireActivity().getSharedPreferences("auth", android.content.Context.MODE_PRIVATE)
        return sharedPreferences.getInt("userId", 0)
    }
}