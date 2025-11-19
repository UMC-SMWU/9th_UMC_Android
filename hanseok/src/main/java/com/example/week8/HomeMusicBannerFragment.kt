package com.example.week8

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.week8.databinding.FragmentHomeMusicBannerBinding

class HomeMusicBannerFragment(
    val topic_str: String,
    val total_song_num: Int,
    val date: String,
    val first_album: Album,
    val second_album: Album): Fragment() {
    lateinit var binding : FragmentHomeMusicBannerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeMusicBannerBinding.inflate(inflater, container, false)

        binding.homePannelTitleTv.text = topic_str
        binding.homePannelAlbumInfo01Tv.text = "총 ${total_song_num}곡 ${date}"
        binding.homePannelAlbumImg01Iv.setImageResource(first_album.coverImg?: R.drawable.img_first_album_default)
        binding.homePannelAlbumImg02Iv.setImageResource(second_album.coverImg?: R.drawable.img_first_album_default)
        binding.homePannelAlbumTitle01Tv.text = first_album.title
        binding.homePannelAlbumTitle02Tv.text = second_album.title
        binding.homePannelAlbumSinger01Tv.text = first_album.singer
        binding.homePannelAlbumSinger02Tv.text = second_album.singer
        return binding.root
    }
}