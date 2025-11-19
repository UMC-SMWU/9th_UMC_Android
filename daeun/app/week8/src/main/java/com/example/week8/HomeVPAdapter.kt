package com.example.week8

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder

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