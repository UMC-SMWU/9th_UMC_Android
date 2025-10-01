package com.example.week3

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumVPAdapter(fragment: Fragment, val songTitle: String?, val singer: String?):
    FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SongFragment()
            1 -> {
                val detailFragment = DetailFragment()
                val sendData = Bundle().apply{
                putString("songTitle", songTitle)
                putString("singer", singer)}
                detailFragment.arguments = sendData
                return detailFragment
            }
            else -> VideoFragment()
        }
    }

    override fun getItemCount(): Int = 3

}