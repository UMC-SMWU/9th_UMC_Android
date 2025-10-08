package com.example.week3

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumVPAdapter(fragment:Fragment, private val bundle : Bundle) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SongFragment().apply { arguments = bundle }
            1 -> DetailFragment().apply { arguments = bundle }
            else -> VideoFragment().apply { arguments = bundle }
        }
    }
    override fun getItemCount(): Int = 3
}