package com.example.yoonseo

import android.R.attr.bottom
import android.graphics.Rect
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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private val tabTitles = arrayOf("수록곡", "상세정보", "영상")

    // Album Header 구성
    private var albumTitle: String? = null
    private var albumArtist: String? = null
    private var albumCoverRes: Int = 0

    // Tab 동기화 위한 Mediator 2개
    private var mediatorForScrolling: TabLayoutMediator? = null
    private var mediatorForPinned: TabLayoutMediator? = null

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

        // 헤더 정보 바인딩
        binding.albumTitle.text = albumTitle ?: "Unknown Album"
        binding.albumSinger.text = albumArtist ?: "Unknown Album"
        binding.albumCoverIv.setImageResource(albumCoverRes)

        // 뒤로가기
        binding.toolbarBackBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // ViewPager2
        binding.albumContentViewpager.isUserInputEnabled = true
        binding.albumContentViewpager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2
            override fun createFragment(position: Int) = when (position) {
                0 -> TracklistFragment()
                else -> AlbumInfoFragment()
            }
        }

        // Tab -> 같은 ViewPager 연결
        mediatorForScrolling = TabLayoutMediator(binding.scrollingTab, binding.albumContentViewpager) { tab, position ->
            tab.text = tabTitles[position]
        }.apply { attach() }

        mediatorForPinned = TabLayoutMediator(binding.pinnedTab, binding.albumContentViewpager) { tab, position ->
            tab.text = tabTitles[position]
        }.apply { attach() }

        // 보수적 동기화
        syncTabs(binding.scrollingTab, binding.pinnedTab)
        syncTabs(binding.pinnedTab, binding.scrollingTab)

        // 스크롤에 따라 고정탭 표시/숨김
        binding.scrollContainer.setOnScrollChangeListener { v, _, _, _, _ ->
            togglePinnedTabIfNeeded()
        }
        vPost { togglePinnedTabIfNeeded() }

        // Edge-to-Edge 인셋 분배
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val status = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val nav = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            // 상단 인셋
            binding.albumAppbar.updatePadding(top = status.top)
            // 하단 인셋
            binding.albumContentViewpager.updatePadding(bottom = nav.bottom)

            insets
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun togglePinnedTabIfNeeded() {
        val pinnedAreaBottom = binding.albumAppbar.bottom     // 툴바(및 핀 탭 영역)의 하단 Y
        val tabLocation = IntArray(2)
        binding.scrollingTab.getLocationOnScreen(tabLocation)
        val scrollingTabTopOnScreen = tabLocation[1]

        // 화면 기준 툴바 하단 위치
        val appbarRect = Rect()
        binding.albumAppbar.getGlobalVisibleRect(appbarRect)
        val appbarBottomOnScreen = appbarRect.bottom

        val shouldPin = scrollingTabTopOnScreen <= appbarBottomOnScreen
        binding.pinnedTab.visibility = if (shouldPin) View.VISIBLE else View.GONE
        binding.scrollingTab.visibility = if (shouldPin) View.INVISIBLE else View.VISIBLE
    }

    private fun syncTabs(src: TabLayout, dest: TabLayout) {
        src.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                dest.selectTab(dest.getTabAt(tab!!.position))
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private inline fun vPost(crossinline block: () -> Unit) {
        binding.root.post { block() }
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