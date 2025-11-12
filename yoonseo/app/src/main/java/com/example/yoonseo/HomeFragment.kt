import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.yoonseo.AlbumFragment
import com.example.yoonseo.R
import com.example.yoonseo.databinding.FragmentHomeBinding
import com.example.yoonseo.home.Playlist
import com.example.yoonseo.home.PlaylistPagerAdapter
import com.example.yoonseo.home.Region
import com.example.yoonseo.home.ReleaseHorizontalAdapter
import com.example.yoonseo.home.ReleaseRepository
import com.example.yoonseo.home.Track
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupHeroPlaylists()
        setupTodayReleases()

        return binding.root
    }

    private fun setupHeroPlaylists() {
        val playlists = listOf(
            Playlist(
                title = "달밤의 감성 산책(1)",
                trackInfo = "총 10곡 2025.03.30",
                tracks = listOf(
                    Track( 1, "라일락", "아이유 (IU)", R.drawable.img_album_exp2),
                    Track( 1, "Butter", "BTS", R.drawable.img_album_exp),
                )
            ),
            Playlist(
                title = "달밤의 감성 산책(2)",
                trackInfo = "총 10곡 2025.03.30",
                tracks = listOf(
                    Track( 1, "라일락", "아이유 (IU)", R.drawable.img_album_exp2),
                    Track( 1, "Butter", "BTS", R.drawable.img_album_exp),
                )
            ),
            Playlist(
                title = "달밤의 감성 산책(3)",
                trackInfo = "총 10곡 2025.03.30",
                tracks = listOf(
                    Track( 1, "라일락", "아이유 (IU)", R.drawable.img_album_exp2),
                    Track( 1, "Butter", "BTS", R.drawable.img_album_exp),
                )
            )
        )
        binding.homePlaylistPager.adapter = PlaylistPagerAdapter(playlists)
        // 캐러셀에 dots 붙이기
        binding.homePlaylistPagerIndicator.attachTo(binding.homePlaylistPager)
    }

    private fun setupTodayReleases() {
        val all = ReleaseRepository.loadToday()

        val adapter = ReleaseHorizontalAdapter(onClick = { album ->
            val fragment = AlbumFragment.newInstance(album.title, album.artist,  album.coverImg)

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, fragment)
                .addToBackStack(null)
                .commit()
        })

        binding.todayReleaseMusicRv.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter

            PagerSnapHelper().attachToRecyclerView(this)

            // 앨범 카드 간격
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val pos = parent.getChildAdapterPosition(view)
                }
            })
        }
        // tab 구성 (종합, 국내, 해외)
        val tabs = binding.todayReleaseTabs
        if (tabs.tabCount == 0) {
            tabs.addTab(tabs.newTab().setText("종합").setTag(Region.ALL))
            tabs.addTab(tabs.newTab().setText("국내").setTag(Region.KOREA))
            tabs.addTab(tabs.newTab().setText("해외").setTag(Region.GLOBAL))
        }
        // 필터 적용 함수
        fun apply(region: Region) {
            val filtered = when (region) {
                Region.ALL -> all
                Region.KOREA -> all.filter { it.region == Region.KOREA }
                Region.GLOBAL -> all.filter { it.region == Region.GLOBAL }
            }
            adapter.submit(filtered)
        }
        apply(Region.ALL)

        // tab 선택 리스너
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val region = tab.tag as? Region ?: Region.ALL
                apply(region)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

}