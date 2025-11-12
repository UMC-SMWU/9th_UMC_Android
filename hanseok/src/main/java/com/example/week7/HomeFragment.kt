package com.example.week7

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.week7.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.gson.Gson
import java.util.ArrayList

class HomeFragment : Fragment() {
  lateinit var binding: FragmentHomeBinding
//  lateinit var albumDB: AlbumDatabase
  private lateinit var albumRVAdapter: AlbumRVAdapter
  private var albumList = ArrayList<Album>()

  private val database = Firebase.database
  private val albumsRef = database.getReference("albums")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
//        albumDB = AlbumDatabase.getInstance(requireContext())!!
//        albumList = albumDB.albumDao().getAlbums() as ArrayList<Album>


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
    override fun onStart(){
        super.onStart()
        initRecyclerview()
        loadAlbumsFromFirebase()
    }

    private fun loadAlbumsFromFirebase() {
        albumsRef.get().addOnSuccessListener { dataSnapshot ->
            albumList.clear()
            for(albumSnapshot in dataSnapshot.children){
                val album = albumSnapshot.getValue(Album::class.java)
                album?.let{
                    albumList.add(it)
                }
            }
            albumRVAdapter.addAlbums(albumList)
            albumRVAdapter.notifyDataSetChanged()


            val homemusicbannerAdapter = HomeMusicBannerVPAdapter(this)
            homemusicbannerAdapter.addFragment(HomeMusicBannerFragment(
                "오늘의 추천 노래",
                17,
                "2025.03.31",
                albumList[0],
                albumList[1]
            ))
            homemusicbannerAdapter.addFragment(HomeMusicBannerFragment(
                "달밤의 감성 산책",
                15,
                "2025.03.30",
                albumList[2],
                albumList[3]
            ))
            homemusicbannerAdapter.addFragment(HomeMusicBannerFragment(
                "!!!!몰라",
                132,
                "2025.05.31",
                albumList[3],
                albumList[3]
            ))
            binding.homeMusicBanner.adapter = homemusicbannerAdapter
            binding.homeMusicBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            // 인디케이터 연결
            val indicator = binding.homeMusicBannerIndicator
            indicator.setViewPager(binding.homeMusicBanner)

        }.addOnFailureListener {
            Log.e("HomeFragment","홈프래그먼트에서 파이어베이스 앨범데이터 로드 실패함")
        }
    }

    private fun initRecyclerview(){
        albumRVAdapter = AlbumRVAdapter()
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        albumList =  albumDB.albumDao().getAlbums() as ArrayList<Album>

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeAlbum(position)
            }

            override fun applyItemToSeekBar(album: Album) {
                TODO("Not yet implemented")
            }
        })

//        albumRVAdapter.addAlbums(albumList)
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
