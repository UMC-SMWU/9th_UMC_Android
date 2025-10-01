package com.example.floapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AlbumFragment: Fragment() {

    private var songTitle: String? = null

    companion object{
        fun newInstance(p: String) = AlbumFragment().apply{
            arguments = Bundle().apply{putString("songTitle", p)}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
        songTitle = it.getString("songTitle").toString()
        Log.e("songTitle", "songTitle of Album Fragment: $songTitle")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.album_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("songTitle", "songTitle of Album Fragment: $songTitle")
        //songTitle이 null이라 해당 코드 시행 X
        if(songTitle != null)
            view.findViewById<TextView>(R.id.album_shuffle_song_title).text = songTitle

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

    }
}
