package com.example.week7

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumVPAdapter(fragment: Fragment, private val songList: ArrayList<Song>?) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SongFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("songList", songList)
                }
            }
            1 -> DetailFragment()
            else -> VideoFragment()
        }
    }
    override fun getItemCount(): Int = 3
}