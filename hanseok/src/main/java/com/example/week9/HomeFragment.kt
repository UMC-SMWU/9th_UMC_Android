package com.example.week9

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.week9.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import java.util.ArrayList

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var albumRVAdapter: AlbumRVAdapter
    private var albumList = ArrayList<Album>()
    private val albumsRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("albums")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart(){
        super.onStart()
        initRecyclerview()
        loadAlbumsFromFirebase()
    }

    private fun initVP(){
        if (albumList.size < 4) return

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

        val indicator = binding.homeMusicBannerIndicator
        indicator.setViewPager(binding.homeMusicBanner)
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
            initVP()

        }.addOnFailureListener {
            Log.e("HomeFragment","홈프래그먼트에서 파이어베이스 앨범데이터 로드 실패함")
        }
    }

    private fun initRecyclerview(){
        albumRVAdapter = AlbumRVAdapter()
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album) {
                loadSongsAndChangeFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeAlbum(position)
            }

            override fun applyItemToSeekBar(album: Album) {
                // TODO("Not yet implemented")
            }
        })
    }

    private fun loadSongsAndChangeFragment(album: Album) {
        val songsRef = FirebaseDatabase.getInstance().getReference("songs")

        songsRef.orderByChild("albumIdx").equalTo(album.albumIdx.toDouble())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val albumSongs = ArrayList<Song>()
                    for (songSnapshot in snapshot.children) {
                        val song = songSnapshot.getValue(Song::class.java)
                        song?.let { albumSongs.add(it) }
                    }
                    album.songs = albumSongs
                    changeAlbumFragment(album)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("HomeFragment", "수록곡 로드 실패", error.toException())
                    changeAlbumFragment(album)
                }
            })
    }

    private fun changeAlbumFragment(album: Album) {
        (context as Week9).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            }).commitAllowingStateLoss()
    }
}
