package com.example.week9_10.ui.main.locker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week9_10.R
import com.example.week9_10.data.entities.Album
import com.example.week9_10.databinding.ItemLockerAlbumBinding

class StoredAlbumRVAdapter(private val storedAlbumList: ArrayList<Album>): RecyclerView.Adapter<StoredAlbumRVAdapter.ViewHolder>() {
    lateinit var albumClickListener: MyItemClickListener

    fun setMyItemListener(itemClickListener: MyItemClickListener){
         albumClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemLockerAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(storedAlbumList[position])
        holder.binding.lockerAlbumSongPlayIv.setOnClickListener {albumClickListener.playAlbum(holder, position)}
        holder.binding.lockerAlbumSongMoreIv.setOnClickListener { albumClickListener.removeAlbum(position) }
    }

    override fun getItemCount(): Int = storedAlbumList.size

    fun playAlbum(holder: ViewHolder, position: Int){
        if(!storedAlbumList[position].isPlaying){
            holder.binding.lockerAlbumSongPlayIv.setImageResource(R.drawable.btn_miniplay_pause)
            storedAlbumList[position].isPlaying = true
        }
        else{
            holder.binding.lockerAlbumSongPlayIv.setImageResource(R.drawable.btn_miniplayer_play)
            storedAlbumList[position].isPlaying = false
        }
    }

    fun removeAlbum(position: Int){
        storedAlbumList.removeAt(position)
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding: ItemLockerAlbumBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.lockerAlbumNameTv.text = album.title
            binding.lockerAlbumSingerTv.text = album.singer
            binding.lockerAlbumIv.setImageResource(album.coverImg!!)
            binding.lockerAlbumDesTv.text = album.description
        }
    }

    interface MyItemClickListener{
        fun playAlbum(holder: ViewHolder, position: Int)
        fun removeAlbum(position: Int)
    }
}