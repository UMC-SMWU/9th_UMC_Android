package com.example.week8

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.postDelayed
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.week8.databinding.ActivityMainBinding
import com.example.week8.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import me.relex.circleindicator.CircleIndicator3
import java.lang.Thread

class HomeFragment: Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumData = ArrayList<Album>()
    private var songData = ArrayList<Song>()
    lateinit var timer: Timer
    private var songDB: SongDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())
        songDB?.albumDao()?.getAlbums()?.let { albumData.addAll(it) }

        val albumRVAdapter = AlbumRVAdapter(requireContext(),albumData)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )
        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun syncInMiniPlayer(album: Album){
                activity?.findViewById<TextView>(R.id.main_miniplayer_title_tv)?.text = album.title
                activity?.findViewById<TextView>(R.id.main_miniplayer_singer_tv)?.text = album.singer
            }
        })

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val homeAdapter = HomeVPAdapter(this)
        val viewPager = binding.homePannelBackgroundVp
        viewPager.adapter = homeAdapter
        val indicator = binding.homeCircleIndicator
        indicator.setViewPager(viewPager)
        timer = Timer(viewPager, homeAdapter)
        timer.start()
        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction().replace(
            R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            }).commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    inner class Timer(val viewPager: ViewPager2, val adapter: HomeVPAdapter) : Thread() {
        private var position = 0
        override fun run() {
            super.run()
            try {
                while (true) {
                    if(position < (adapter.itemCount-1)){
                        sleep(3000)
                        position += 1
                        activity?.runOnUiThread{
                            viewPager.currentItem = position
                        }
                    }
                    else{
                        sleep(3000)
                        position = 0
                        activity?.runOnUiThread{
                            viewPager.currentItem = position
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("Song", "The thread is dead ${e.message}")
            }
        }
    }
}
