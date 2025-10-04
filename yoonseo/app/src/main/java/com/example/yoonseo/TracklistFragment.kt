package com.example.yoonseo

import android.R.attr.track
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yoonseo.databinding.FragmentTracklistBinding
import com.example.yoonseo.home.Track

/**
 * AlbumFragment의 "수록곡" 탭 내용:
 * 상단에 "내 취향 MIX", 아래 RecyclerView 트랙 리스트
 */
class TracklistFragment : Fragment() {

    private var _binding: FragmentTracklistBinding? = null
    private val binding get() = _binding!!

    private var isMixOn = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTracklistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 더미 트랙
        val tracks = listOf(
            Track("Solitary Moon", "Moon (혜원) & Tsuyoshi Yamamoto", R.drawable.fascination_moon),
            Track("Dream a Little Dream of Me", "Moon (혜원) & Tsuyoshi Yamamoto", R.drawable.fascination_moon),
            Track("Fascination", "Moon (혜원) & Tsuyoshi Yamamoto", R.drawable.fascination_moon),
            Track("Early Autumn", "Moon (혜원) & Tsuyoshi Yamamoto", R.drawable.fascination_moon),
            Track("Once in a While", "Moon (혜원) & Tsuyoshi Yamamoto", R.drawable.fascination_moon),
            Track("The Very Thought of You", "Moon (혜원) & Tsuyoshi Yamamoto", R.drawable.fascination_moon),
            Track("Water Fog (물안개)", "Moon (혜원) & Tsuyoshi Yamamoto", R.drawable.fascination_moon)
        )

        binding.tracklistRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }

        // MIX 토글
        binding.tracklistMixBtn.setOnClickListener {
            isMixOn = !isMixOn
            updateMixButtonImage()
            // TODO: MIX 로직 연결
        }
    }

    private fun updateMixButtonImage() {
        val iconRes = if(isMixOn) R.drawable.btn_toggle_on else R.drawable.btn_toggle_off
        binding.tracklistMixBtn.findViewById<ImageButton>(R.id.tracklist_mix_btn).setImageResource(iconRes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}