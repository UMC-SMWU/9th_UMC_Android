package com.example.week3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week3.databinding.FragmentDefaultBinding

class DefaultFragment: Fragment() {
    lateinit var binding: FragmentDefaultBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDefaultBinding.inflate(inflater, container, false)
        return binding.root
    }

}