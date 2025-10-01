package com.example.floapp

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class HomeFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val songTitle = view.findViewById<TextView>(R.id.today_release_song_title).text.toString()
        Log.e("songTitle","songTitle of Home Fragment: $songTitle")
        val albumFragment = AlbumFragment.newInstance(songTitle)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragmentContainer, albumFragment)
            .addToBackStack(null)
            .commit()
    }
}
