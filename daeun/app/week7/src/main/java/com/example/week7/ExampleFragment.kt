package com.example.week7

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week7.databinding.FragmentExampleBinding

class ExampleFragment: Fragment(){
    lateinit var binding: FragmentExampleBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExampleBinding.inflate(inflater, container, false)
        return binding.root
    }
}