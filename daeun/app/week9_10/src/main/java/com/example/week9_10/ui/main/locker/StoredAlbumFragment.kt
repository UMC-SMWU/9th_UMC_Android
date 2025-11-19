package com.example.week9_10.ui.main.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week9_10.SongDatabase
import com.example.week9_10.data.entities.Album
import com.example.week9_10.databinding.FragmentStoredAlbumBinding

class StoredAlbumFragment: Fragment() {
    lateinit var binding: FragmentStoredAlbumBinding
    private var albumList = ArrayList<Album>()
    lateinit var albumDB: SongDatabase
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoredAlbumBinding.inflate(inflater, container, false)
        albumDB = SongDatabase.Companion.getInstance(requireContext())!!
        val userId = getJwt()
        albumList = albumDB.albumDao().getLikedAlbums(userId) as ArrayList

        val adapter = StoredAlbumRVAdapter(albumList)
        binding.storedAlbumContentRv.adapter = adapter
        binding.storedAlbumContentRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)
        adapter.setMyItemListener(object: StoredAlbumRVAdapter.MyItemClickListener{
            override fun playAlbum(holder: StoredAlbumRVAdapter.ViewHolder, position: Int){
                adapter.playAlbum(holder, position)
            }
            override fun removeAlbum(position: Int){
                albumDB.albumDao().disLikedAlbum(userId, albumList[position].id)
                adapter.removeAlbum(position)
            }
        })
        return binding.root
    }
    private fun getJwt(): Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }
}