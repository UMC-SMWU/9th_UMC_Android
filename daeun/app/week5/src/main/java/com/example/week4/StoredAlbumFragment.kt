package com.example.week4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week4.databinding.FragmentStoredAlbumBinding

class StoredAlbumFragment: Fragment() {
    lateinit var binding: FragmentStoredAlbumBinding
    private val albumList = ArrayList<Album>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoredAlbumBinding.inflate(inflater, container, false)
        albumList.apply{
            add(Album("Butter", "방탄소년단(BTS)", R.drawable.img_album_exp, "2021.05.21: 디지털 싱글|KPOP"))
            add(Album("LILAC", "아이유(IU)", R.drawable.img_album_exp2, "2021.03.25: 정규 5집|KPOP"))
        }
        val adapter = StoredAlbumRVAdapter(albumList)
        binding.storedAlbumContentRv.adapter = adapter
        binding.storedAlbumContentRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)
        adapter.setMyItemListener(object: StoredAlbumRVAdapter.MyItemClickListener{
            override fun playAlbum(holder: StoredAlbumRVAdapter.ViewHolder, position: Int){
                adapter.playAlbum(holder, position)
            }
            override fun removeAlbum(position: Int){
                adapter.removeAlbum(position)
            }
        })
        return binding.root
    }
}