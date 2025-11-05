package com.example.week6

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week6.databinding.ItemStorageSongBinding

class StorageSongRVAdapter(
    private val storageSongList: ArrayList<StorageSong>
) : RecyclerView.Adapter<StorageSongRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(song: StorageSong)
        fun onRemoveItem(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun addItem(song: StorageSong) {
        storageSongList.add(song)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        storageSongList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, storageSongList.size) // 포지션만 재활용하면 값의 범위에 잘 반영이 안되는
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStorageSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(storageSongList[position])
        holder.binding.songMore01Iv.setOnClickListener {
            removeItem(position)
        }
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(storageSongList[position])
        }
    }

    override fun getItemCount(): Int = storageSongList.size

    inner class ViewHolder(val binding: ItemStorageSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: StorageSong) {
            song.coverImgResId?.let { binding.itemStorageSongCoverImgIv.setImageResource(it) }
            binding.storageSongMusicTitleTv.text = song.title
            binding.songSingerNameTv.text = song.singer
        }
    }
}
