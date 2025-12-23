package com.example.week9

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week9.databinding.ItemAlbumBinding

class AlbumRVAdapter(): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    private val albums = ArrayList<Album>()

    interface MyItemClickListener {
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)

        fun applyItemToSeekBar(album:Album)
    }
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener= itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums: ArrayList<Album>){
        this.albums.clear()
        this.albums.addAll(albums)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeAlbum(position: Int) {
        albums.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, albums.size)
    }

    fun applyItemToSeekBar(album:Album){

    }
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albums[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(albums[position]) }
        holder.binding.itemAlbumPlayImgIv.setOnClickListener { mItemClickListener.applyItemToSeekBar(albums[position]) }
    }

    override fun getItemCount(): Int = albums.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            album.coverImg?.let { binding.itemAlbumCoverImgIv.setImageResource(it) }
        }
    }
}