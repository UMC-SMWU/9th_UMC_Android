package com.example.week4

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.week4.databinding.FragmentHomeBinding
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

        albumDatas.apply{
            add(Album("Butter", "방탄소년단", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유", R.drawable.img_album_exp2))
            add(Album("Next Level", "에스파", R.drawable.img_album_exp3))
            add(Album("Boy with Luv", "방탄소년단", R.drawable.img_album_exp4))
            add(Album("BBoom BBoom", "모모랜드", R.drawable.img_album_exp5))
            add(Album("Weekend", "태연", R.drawable.img_album_exp6))

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
                (activity as Week4).setPlayerData(album) // 이 부분도 requireActivity()방식으로 작성하면 null안정성이 있다고는 하는데, 정확히 어떻게 안정성이 있는지 궁금해서 질문드립니다!
                // 어차피 week4의 xml파일에 있는 정보를 수정해야하는데, 반드시 wee4액티비티의 메서드를 다루기 위해 (activity as Week4)이렇게 작성해야하는거 아닌가요? 앱이 강제종료 되기를 막기위해 그렇게 작성하는건가요?
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
