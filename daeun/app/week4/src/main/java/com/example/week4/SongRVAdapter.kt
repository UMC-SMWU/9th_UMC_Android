package com.example.week4

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week4.databinding.ItemAlbumBinding
import com.example.week4.databinding.ItemSongBinding

class SongRVAdapter(private val songList: ArrayList<Song>): RecyclerView.Adapter<SongRVAdapter.ViewHolder>(){
    interface MyItemClickListener{
        fun onRemoveAlbum(position: Int)
    }
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): SongRVAdapter.ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongRVAdapter.ViewHolder, position: Int) {
        holder.bind(songList[position])
        holder.binding.songMore01Iv.setOnClickListener { mItemClickListener.onRemoveAlbum(position) }
    }

    override fun getItemCount(): Int = songList.size

    fun addItem(song: Song){
        songList.add(song)
        notifyDataSetChanged()
    }
    fun removeItem(position: Int){
        songList.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            binding.songMusicTitle01Tv.text = song.title
            binding.songSingerName01Tv.text = song.singer
        }
    }
}