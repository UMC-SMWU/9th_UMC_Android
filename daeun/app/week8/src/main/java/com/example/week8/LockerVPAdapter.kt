package com.example.week8

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> StoredSongFragment()
            1 -> ExampleFragment()
            else -> StoredAlbumFragment()
        }
    }

    override fun getItemCount(): Int = 3

}