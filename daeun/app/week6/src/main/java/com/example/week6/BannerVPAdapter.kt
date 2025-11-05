package com.example.week6

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment){

    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    override fun getItemCount(): Int {
        // how many data are passed
        return fragmentList.size
    }

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size-1)     // notify to viewPager
    }
}