package com.example.week9_10.ui.main.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> DefaultFragment()
            1 -> ExampleFragment()
            else -> ExampleFragment()
        }
    }
    override fun getItemCount(): Int = 3
}