package com.example.week3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.week3.databinding.ActivityWeek3Binding
import com.example.week3.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
  lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        //이부분을 데이터베이스의 인덱스로 접근하는 방식으로 바꿔서 다른 앨범에도 적용시켜야겠다 - 나중에하기
        binding.homeAlbumImgIv1.setOnClickListener {
            val title = binding.homeAlbumImgTv1Title.text.toString()
            val singer = binding.homeAlbumImgTv1Singer.text.toString()
            val albumFragment = AlbumFragment()
            // 프래그먼트끼리 주고받을 때는 bundle사용하기
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("singer", singer)
            Log.d("HomeFragment", "title=$title, singer=$singer")
            albumFragment.arguments = bundle

            parentFragmentManager.beginTransaction() //클론코딩 영상에서 context를 사용하던데 안쓰는것이 더 안정성 있겠군요
                .replace(R.id.main_frm,
                albumFragment).commitAllowingStateLoss() // 이부분에서 실수함
        }

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val homebannerAdapter = HomeBannerVPAdapter(this)
        homebannerAdapter.addFragment(HomeBannerFragment(R.drawable.img_first_album_default))
        homebannerAdapter.addFragment(HomeBannerFragment(R.drawable.img_first_album_default))
        binding.homePannelBackgroundIv.adapter = homebannerAdapter
        binding.homePannelBackgroundIv.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        return binding.root
    }
}