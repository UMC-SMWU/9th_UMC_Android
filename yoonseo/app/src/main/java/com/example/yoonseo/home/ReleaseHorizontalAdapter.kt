package com.example.yoonseo.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yoonseo.R
import com.example.yoonseo.databinding.ItemTodayReleaseAlbumBinding

/**
 * 오늘 발매 음악 가로 스와이프 리스트 어댑터
 */
class ReleaseHorizontalAdapter (
    private var items: List<ReleaseAlbum> = emptyList(),
    private val onClick: (ReleaseAlbum) -> Unit = {}
) : RecyclerView.Adapter<ReleaseHorizontalAdapter.VH>() {

    /*
    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val coverIv: ImageView = view.findViewById(R.id.albumCoverIv)
        val titleTv: TextView = view.findViewById(R.id.albumTitleTv)
        val singerTv: TextView = view.findViewById(R.id.albumArtistTv)

        init {
            view.setOnClickListener {
                val pos = bindingAdapterPosition
                if(pos != RecyclerView.NO_POSITION) {
                    onClick(items[pos])
                }
            }
        }
    }
    */

    inner class VH(private val binding: ItemTodayReleaseAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReleaseAlbum) {
            binding.albumCoverIv.setImageResource(item.coverImg)
            binding.albumTitleTv.text = item.title
            binding.albumArtistTv.text = item.artist

            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    fun submit(list: List<ReleaseAlbum>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        /*
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_today_release_album, parent, false)
        return VH(binding)
         */
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTodayReleaseAlbumBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        /*
        val item = items[position]
        holder.coverIv.setImageResource(item.coverImg)
        holder.titleTv.text = item.title
        holder.singerTv.text = item.artist
        */
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}