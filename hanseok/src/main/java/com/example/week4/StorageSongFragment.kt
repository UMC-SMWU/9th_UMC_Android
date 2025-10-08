package com.example.week4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week4.databinding.FragmentStorageSongBinding

class StorageSongFragment : Fragment() {

    private lateinit var binding: FragmentStorageSongBinding
    private var storageSongList = ArrayList<StorageSong>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStorageSongBinding.inflate(inflater, container, false)


        storageSongList.apply {
            add(StorageSong(R.drawable.img_album_exp, "Butter", "방탄소년단"))
            add(StorageSong(R.drawable.img_album_exp2, "Lilac", "아이유"))
            add(StorageSong(R.drawable.img_album_exp3, "Next Level", "에스파"))
            add(StorageSong(R.drawable.img_album_exp4, "Boy with Luv", "방탄소년단"))
            add(StorageSong(R.drawable.img_album_exp5, "BBoom BBoom", "모모랜드"))
            add(StorageSong(R.drawable.img_album_exp6, "Weekend", "태연"))
            add(StorageSong(R.drawable.img_album_exp, "Butter", "방탄소년단"))
            add(StorageSong(R.drawable.img_album_exp2, "Lilac", "아이유"))
            add(StorageSong(R.drawable.img_album_exp3, "Next Level", "에스파"))
            add(StorageSong(R.drawable.img_album_exp4, "Boy with Luv", "방탄소년단"))
            add(StorageSong(R.drawable.img_album_exp5, "BBoom BBoom", "모모랜드"))
            add(StorageSong(R.drawable.img_album_exp6, "Weekend", "태연"))

        }

        val storageSongAdapter = StorageSongAdapter(storageSongList)
        binding.storageSongRv.adapter = storageSongAdapter
        binding.storageSongRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)


        storageSongAdapter.setMyItemClickListener(object : StorageSongAdapter.MyItemClickListener {
            override fun onItemClick(song: StorageSong) {

            }

            override fun onRemoveItem(position: Int) {
                storageSongAdapter.removeItem(position)
            }
        })

        return binding.root
    }
}
