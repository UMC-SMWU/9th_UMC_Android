package com.example.week3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week3.databinding.FragmentStoredSongBinding

class StoredSongFragment: Fragment() {
    lateinit var binding: FragmentStoredSongBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoredSongBinding.inflate(inflater, container, false)
        return binding.root
    }
}