package com.example.week6

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week6.databinding.ItemStorageAlbumBinding

class StorageAlbumRVAdapter(private var albumList: ArrayList<StorageAlbum>) : RecyclerView.Adapter<StorageAlbumRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onRemoveAlbum(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, albumList.size)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemStorageAlbumBinding = ItemStorageAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.binding.songMore01Iv.setOnClickListener {
            mItemClickListener.onRemoveAlbum(position)
        }
        holder.binding.songPlay01Iv.setOnClickListener {
            val song = albumList[position]
            song.isPlaying = !song.isPlaying
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemStorageAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: StorageAlbum) {
            binding.storageSongMusicTitleTv.text = album.title
            binding.songSingerNameTv.text = album.singer
            album.coverImg?.let { binding.itemStorageSongCoverImgIv.setImageResource(it) }
            binding.songDetailInfoTv.text = album.info
            if (album.isPlaying) {
                binding.songPlay01Iv.setImageResource(R.drawable.btn_miniplay_pause)
            } else {
                binding.songPlay01Iv.setImageResource(R.drawable.btn_player_play)
            }

// 이렇게 하면 안되는 이유를 모르겠음
//            if (album.isPlaying) {
//                binding.songPlay01Iv.visibility = View.GONE
//                binding.songPauseIv.visibility = View.VISIBLE
//            } else {
//                binding.songPlay01Iv.visibility = View.VISIBLE
//                binding.songPauseIv.visibility = View.GONE
//            }
        }
    }
}
