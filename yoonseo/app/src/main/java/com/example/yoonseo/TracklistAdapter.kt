package com.example.yoonseo

import androidx.fragment.app.Fragment
import com.example.yoonseo.databinding.FragmentTracklistBinding

class TracklistAdapter : Fragment() {
    private var _binding: FragmentTracklistBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TracklistAdapter
    private var isMixOn = false


}