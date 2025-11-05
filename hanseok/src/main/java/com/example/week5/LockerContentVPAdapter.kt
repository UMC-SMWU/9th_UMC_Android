package com.example.week5

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerContentVPAdapter(fragment:Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> StorageSongFragment()
            1 -> SongFileFragment()
            else -> StorageAlbumFragment()
        }
    }

    override fun getItemCount(): Int = 3
}