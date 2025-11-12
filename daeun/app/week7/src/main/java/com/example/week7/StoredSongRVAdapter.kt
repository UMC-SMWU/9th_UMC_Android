package com.example.week7

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week7.databinding.ItemSongBinding

class StoredSongRVAdapter(context: Context, private val songList: ArrayList<Song>): RecyclerView.Adapter<StoredSongRVAdapter.ViewHolder>(){
    interface MyItemClickListener{
        fun onRemoveAlbum(position: Int)
    }
    private lateinit var mItemClickListener: MyItemClickListener
    private val albumDB = AlbumDatabase.getInstance(context)
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): StoredSongRVAdapter.ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoredSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(songList[position])
        holder.binding.songMore01Iv.setOnClickListener {
            mItemClickListener.onRemoveAlbum(position)
        }
    }

    override fun getItemCount(): Int = songList.size

    fun removeItem(position: Int){
        val songId = songList[position].id
        songList.removeAt(position)
        albumDB?.songDao()?.updateIsLikeById(false, songId)
        notifyDataSetChanged()
    }

    fun addSongs(songs: ArrayList<Song>){
        songList.clear()
        songList.addAll(songs)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            binding.songMusicTitle01Tv.text = song.title
            binding.songSingerName01Tv.text = song.singer
        }
    }
}