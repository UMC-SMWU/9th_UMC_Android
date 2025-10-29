package com.example.week5

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week5.databinding.FragmentStorageAlbumBinding

class StorageAlbumFragment : Fragment() {

    private lateinit var binding: FragmentStorageAlbumBinding
    private var albumList = ArrayList<StorageAlbum>()
    private lateinit var albumRVAdapter: StorageAlbumRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStorageAlbumBinding.inflate(inflater, container, false)

        albumList.apply {
            add(StorageAlbum("Butter", "방탄소년단", R.drawable.img_album_exp, "정규"))
            add(StorageAlbum("Lilac", "아이유", R.drawable.img_album_exp2, "정규"))
            add(StorageAlbum("Next Level", "에스파", R.drawable.img_album_exp3, "싱글"))
            add(StorageAlbum("Boy with Luv", "방탄소년단", R.drawable.img_album_exp4, "EP"))
            add(StorageAlbum("BBoom BBoom", "모모랜드", R.drawable.img_album_exp5, "미니"))
            add(StorageAlbum("Weekend", "태연", R.drawable.img_album_exp6, "싱글"))
        }

        albumRVAdapter = StorageAlbumRVAdapter(albumList)
        binding.storageAlbumRv.adapter = albumRVAdapter
        binding.storageAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        albumRVAdapter.setMyItemClickListener(object : StorageAlbumRVAdapter.MyItemClickListener {
            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })

        return binding.root
    }
}
