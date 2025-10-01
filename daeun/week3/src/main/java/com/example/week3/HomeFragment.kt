package com.example.week3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.week3.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.zip.Inflater

class HomeFragment: Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val albumFragment = AlbumFragment()
        val sendData = Bundle().apply{
            putString("songTitle", binding.homeAlbumSongTitleTv1.text.toString())
            putString("singer", binding.homeAlbumSingerTv1.text.toString())
        }
        albumFragment.arguments = sendData
        binding.homeAlbumImgIv1.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(
                R.id.main_frm, albumFragment).commitAllowingStateLoss()
        }

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
}