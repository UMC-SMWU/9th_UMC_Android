package com.example.week8

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week8.databinding.ItemStorageSongBinding

class StorageSongRVAdapter() : RecyclerView.Adapter<StorageSongRVAdapter.ViewHolder>() {

    private val songs: ArrayList<Song> = ArrayList()

    interface MyItemClickListener {
        fun onItemClick(song: Song)
        fun onRemoveSong(songId: String)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStorageSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.binding.songMore01Iv.setOnClickListener {
            mItemClickListener.onRemoveSong(songs[position].id)
            removeSong(position)
        }
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(songs[position])
        }
    }

    override fun getItemCount(): Int = songs.size

    inner class ViewHolder(val binding: ItemStorageSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            song.coverImg?.let { binding.itemStorageSongCoverImgIv.setImageResource(it) }
            binding.storageSongMusicTitleTv.text = song.title
            binding.songSingerNameTv.text = song.singer
        }
    }
}
