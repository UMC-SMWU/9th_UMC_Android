package com.example.week4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.week4.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import java.util.zip.Inflater

class HomeFragment: Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumData = ArrayList<Album>()
    private var songData = ArrayList<Song>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        albumData.apply{
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
        }
        songData.apply{
            add(Song("LILAC", "아이유(IU)"))
            add(Song("Flu", "아이유(IU)"))
            add(Song("Coin", "아이유(IU)"))
            add(Song("봄 안녕 봄", "아이유(IU)"))
            add(Song("Celebrity", "아이유(IU)"))
        }

        val albumRVAdapter = AlbumRVAdapter(albumData)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album){
                changeAlbumFragment(album)
            }

            override fun syncInMiniPlayer(album: Album) {
                activity?.findViewById<TextView>(R.id.main_miniplayer_title_tv)!!.text = album.title
                activity?.findViewById<TextView>(R.id.main_miniplayer_singer_tv)!!.text = album.singer
            }
        })

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val homeAdapter = HomeVPAdapter(this)
        binding.homePannelBackgroundVp.adapter = homeAdapter
        TabLayoutMediator(binding.homePanelTb, binding.homePannelBackgroundVp){
                tab, position ->
            tab.setIcon(R.drawable.selected_circle_12)
        }.attach()
        return binding.root
    }

    private fun changeAlbumFragment(album: Album){
        (context as MainActivity).supportFragmentManager.beginTransaction().replace(
            R.id.main_frm, AlbumFragment().apply{
                arguments = Bundle().apply{
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    //val songJson = gson.toJson(songList)
                    putString("album", albumJson)
                }
            }).commitAllowingStateLoss()
    }
}