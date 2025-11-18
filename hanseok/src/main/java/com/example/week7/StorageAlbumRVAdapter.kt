package com.example.week7

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week7.databinding.ItemStorageAlbumBinding

class StorageAlbumRVAdapter() : RecyclerView.Adapter<StorageAlbumRVAdapter.ViewHolder>() {

    private val songs = ArrayList<Song>()

    interface MyItemClickListener {
        fun onRemoveAlbum(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>){
        this.songs.clear()
        this.songs.addAll(songs)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeSong(position: Int) {
        songs.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, songs.size)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemStorageAlbumBinding = ItemStorageAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.binding.songMore01Iv.setOnClickListener {
            mItemClickListener.onRemoveAlbum(position)
        }
        holder.binding.songPlay01Iv.setOnClickListener {
            val song = songs[position]
            song.isPlaying = !song.isPlaying
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = songs.size

    inner class ViewHolder(val binding: ItemStorageAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.storageSongMusicTitleTv.text = song.title
            binding.songSingerNameTv.text = song.singer
            song.coverImg?.let { binding.itemStorageSongCoverImgIv.setImageResource(it) }
            binding.songDetailInfoTv.text = song.singer // Use singer as a placeholder for info
            if (song.isPlaying) {
                binding.songPlay01Iv.setImageResource(R.drawable.btn_miniplay_pause)
            } else {
                binding.songPlay01Iv.setImageResource(R.drawable.btn_player_play)
            }

        }
    }



}
