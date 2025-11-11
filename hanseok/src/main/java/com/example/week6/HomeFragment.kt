package com.example.week6

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.week6.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : Fragment() {
  lateinit var binding: FragmentHomeBinding
  private var albumDatas = ArrayList<Album>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        //이부분을 데이터베이스의 인덱스로 접근하는 방식으로 바꿔서 다른 앨범에도 적용시켜야겠다 - 나중에하기
//        binding.homeAlbumImgIv1.setOnClickListener {
//            val title = binding.homeAlbumImgTv1Title.text.toString()
//            val singer = binding.homeAlbumImgTv1Singer.text.toString()
//            val albumFragment = AlbumFragment()
            // 프래그먼트끼리 주고받을 때는 bundle사용하기
        val first_album: Album = Album("Butter", "방탄소년단", R.drawable.img_album_exp)
        val second_album: Album = Album("Lilac", "아이유", R.drawable.img_album_exp2)
        val third_album: Album = Album("Next Level", "에스파", R.drawable.img_album_exp3)
        val fourth_album: Album = Album("Boy with Luv", "방탄소년단", R.drawable.img_album_exp4)
        val fifth_album: Album = Album("BBoom BBoom", "모모랜드", R.drawable.img_album_exp5)
        val sixth_album: Album = Album("Weekend", "태연", R.drawable.img_album_exp6)
        albumDatas.apply{
            add(first_album)
            add(second_album)
            add(third_album)
            add(fourth_album)
            add(fifth_album)
            add(sixth_album)

        }
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }

            override fun applyItemToSeekBar(album: Album) {
                (activity as Week6).setPlayerData(album)
            }
        })

//        val bundle = Bundle()
//        bundle.putString("title", title)
//        bundle.putString("singer", singer)
//        Log.d("HomeFragment", "title=$title, singer=$singer")
//        albumFragment.arguments = bundle
//
//        parentFragmentManager.beginTransaction() //클론코딩 영상에서 context를 사용하던데 안쓰는것이 더 안정성 있겠군요
//            .replace(R.id.main_frm,
//                albumFragment).commitAllowingStateLoss() // 이부분에서 실수함

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

        // 위에꺼(homebanner) 이미지로 바꿔야하는데 이왕 바꾼김에 그냥 놔둘께요 파란색 배경부분입니다.

        // 이번 과제로 나온 인디케이터 쓰는 VP는 아래꺼(homemusicbanner)입니다.
        val homemusicbannerAdapter = HomeMusicBannerVPAdapter(this)
        homemusicbannerAdapter.addFragment(HomeMusicBannerFragment(
            "오늘의 추천 노래",
            17,
            "2025.03.31",
            first_album,
            second_album
        ))
        homemusicbannerAdapter.addFragment(HomeMusicBannerFragment(
            "달밤의 감성 산책",
            15,
            "2025.03.30",
            third_album,
            fourth_album
        ))
        homemusicbannerAdapter.addFragment(HomeMusicBannerFragment(
            "!!!!몰라",
            132,
            "2025.05.31",
            fifth_album,
            sixth_album
        ))
        binding.homeMusicBanner.adapter = homemusicbannerAdapter
        binding.homeMusicBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 인디케이터 연결
        val indicator = binding.homeMusicBannerIndicator
        indicator.setViewPager(binding.homeMusicBanner)

        return binding.root
    }

}

private fun HomeFragment.changeAlbumFragment(album: Album) {
    requireActivity().supportFragmentManager.beginTransaction()
        .replace(R.id.main_frm, AlbumFragment().apply {
            arguments = Bundle().apply {
                val gson = Gson()
                val albumJson = gson.toJson(album)
                putString("album", albumJson)
            }
        })
        .commitAllowingStateLoss()
}
