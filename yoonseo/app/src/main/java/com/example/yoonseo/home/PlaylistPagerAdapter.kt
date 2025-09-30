package com.example.yoonseo.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yoonseo.R

class PlaylistPagerAdapter(
    private val items: List<Playlist>
) : RecyclerView.Adapter<PlaylistPagerAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val titleTv: TextView = view.findViewById(R.id.playlist_title)
        val trackDataTv: TextView = view.findViewById(R.id.playlist_track_data)
        val tracksContainer: ViewGroup = view.findViewById(R.id.tracksContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_hero_playlist, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val playlist = items[position]
        holder.titleTv.text = playlist.title
        holder.trackDataTv.text = playlist.trackInfo

        // tracksContainer 비우고 다시 채우기
        holder.tracksContainer.removeAllViews()
        val inflater = LayoutInflater.from(holder.itemView.context)

        playlist.tracks.take(2).forEach { track ->
            val trackView = inflater.inflate(R.layout.item_track_row, holder.tracksContainer, false)
            val titleTv = trackView.findViewById<TextView>(R.id.trackTitleTv)
            val artistTv = trackView.findViewById<TextView>(R.id.trackArtistTv)
            val albumIv = trackView.findViewById<ImageView>(R.id.trackAlbumIv)

            titleTv.text = track.title
            artistTv.text = track.artist
            albumIv.setImageResource(track.albumCover)

            holder.tracksContainer.addView(trackView)
        }
    }

    override fun getItemCount(): Int = items.size
}