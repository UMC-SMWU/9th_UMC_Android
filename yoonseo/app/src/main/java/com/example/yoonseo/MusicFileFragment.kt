package com.example.yoonseo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yoonseo.databinding.FragmentLockerMusicfileBinding

class MusicFileFragment: Fragment() {

    lateinit var binding: FragmentLockerMusicfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLockerMusicfileBinding.inflate(inflater, container, false)

        return binding.root
    }
}