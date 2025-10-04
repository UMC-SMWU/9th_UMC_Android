package com.example.yoonseo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.yoonseo.databinding.FragmentAlbumBinding
import com.example.yoonseo.databinding.FragmentSearchBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    // 탭 타이틀
    private val tabTitles = arrayOf("수록곡", "상세정보", "영상")

    // 앨범 데이터
    private var albumTitle: String? = null
    private var albumArtist: String? = null
    private var albumCoverRes: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            albumTitle = it.getString("album_title")
            albumArtist = it.getString("album_artist")
            albumCoverRes = it.getInt("album_cover")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // CollapsingToolbar에 표시될 앨범명
        binding.collapsingToolbar.title = albumTitle ?: "Unknown Album"

        // 툴바 내 뒤로가기 버튼 클릭
        binding.toolbarBackBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // ViewPager2 + Tab
        binding.albumContentViewpager.adapter = AlbumPagerAdaptor(this)
        TabLayoutMediator(binding.albumTabLayout, binding.albumContentViewpager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        // 엣지-투-엣지 인셋 분배
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val status = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val nav = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            // 상단 인셋은 AppBar에만
            binding.albumAppBar.updatePadding(top = status.top)
            // 하단 인셋은 스크롤 컨텐츠에만
            binding.albumContentViewpager.updatePadding(bottom = nav.bottom)
            binding.albumTabLayout.updatePadding(top = 0, bottom = 0)

            insets
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class AlbumPagerAdaptor(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2
        override fun createFragment(position: Int): Fragment =
            when (position) {
                0 -> TracklistFragment()
                else -> Fragment()
            }
    }

    companion object {
        fun newInstance(title: String, artist: String, coverImg: Int) =
            AlbumFragment().apply {
                arguments = Bundle().apply {
                    putString("album_title", title)
                    putString("album_artist", artist)
                    putInt("album_cover", coverImg)
                }
            }
    }

}