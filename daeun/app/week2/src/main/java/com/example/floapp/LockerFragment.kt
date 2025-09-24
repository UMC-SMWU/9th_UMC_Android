package com.example.floapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class LockerFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.album_fragment, container, false)
        val mainBnv: BottomNavigationView = view.findViewById(R.id.main_bnv)
        // 시행 안 됨
        mainBnv.setOnClickListener { item ->
            when(item.id){
                R.id.home -> {
                    (activity as FragmentActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragmentContainer, HomeFragment())
                        .commit()
                }
                R.id.personal_storage -> {
                    (activity as FragmentActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragmentContainer, LockerFragment())
                        .commit()
                }
            }
        }
        return view
    }
}