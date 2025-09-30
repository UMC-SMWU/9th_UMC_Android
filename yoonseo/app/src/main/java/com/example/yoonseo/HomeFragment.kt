import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yoonseo.R
import com.example.yoonseo.databinding.FragmentHomeBinding
import com.example.yoonseo.home.Playlist
import com.example.yoonseo.home.PlaylistPagerAdapter
import com.example.yoonseo.home.Track

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupHeroPlaylists()

        return binding.root
    }

    private fun setupHeroPlaylists() {
        val playlists = listOf(
            Playlist(
                title = "달밤의 감성 산책(1)",
                trackInfo = "총 10곡 2025.03.30",
                tracks = listOf(
                    Track( "라일락", "아이유 (IU)", R.drawable.img_album_exp2),
                    Track( "Butter", "BTS", R.drawable.img_album_exp),
                )
            ),
            Playlist(
                title = "달밤의 감성 산책(2)",
                trackInfo = "총 10곡 2025.03.30",
                tracks = listOf(
                    Track( "라일락", "아이유 (IU)", R.drawable.img_album_exp2),
                    Track( "Butter", "BTS", R.drawable.img_album_exp),
                )
            ),
            Playlist(
                title = "달밤의 감성 산책(3)",
                trackInfo = "총 10곡 2025.03.30",
                tracks = listOf(
                    Track( "라일락", "아이유 (IU)", R.drawable.img_album_exp2),
                    Track( "Butter", "BTS", R.drawable.img_album_exp),
                )
            )
        )
        binding.homePlaylistPager.adapter = PlaylistPagerAdapter(playlists)
    }

}