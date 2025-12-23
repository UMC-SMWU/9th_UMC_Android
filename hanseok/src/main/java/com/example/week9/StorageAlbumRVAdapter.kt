package com.example.week9

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week9.databinding.ItemStorageAlbumBinding

class StorageAlbumRVAdapter() : RecyclerView.Adapter<StorageAlbumRVAdapter.ViewHolder>() {

    private val albums = ArrayList<Album>()

    interface MyItemClickListener {
        fun onRemoveAlbum(albumId: String)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
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

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemStorageAlbumBinding = ItemStorageAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albums[position])

        holder.binding.songMore01Iv.setOnClickListener {
            mItemClickListener.onRemoveAlbum(albums[position].id) // 앨범의 고유 ID 전달
        }

        holder.binding.songPlay01Iv.setOnClickListener {
            // 아직 아무동작 안함 앨범이라서 나중에 수록곡 있는 화면으로 보낼예정
        }
    }

    override fun getItemCount(): Int = albums.size

    inner class ViewHolder(val binding: ItemStorageAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.storageSongMusicTitleTv.text = album.title
            binding.songSingerNameTv.text = album.singer
            album.coverImg?.let { binding.itemStorageSongCoverImgIv.setImageResource(it) }

            binding.songDetailInfoTv.text = "앨범"
            binding.songPlay01Iv.setImageResource(R.drawable.btn_player_play)
        }
    }
}
