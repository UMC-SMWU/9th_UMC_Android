package com.example.week5

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week5.databinding.ItemAlbumBinding

class AlbumRVAdapter(private var albumList:ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)

        fun applyItemToSeekBar(album:Album)
    }
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener= itemClickListener
    }

    fun addItem(album:Album){
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        albumList.removeAt(position )
        notifyDataSetChanged()
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
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(albumList[position]) }
        holder.binding.itemAlbumPlayImgIv.setOnClickListener { mItemClickListener.applyItemToSeekBar(albumList[position]) }

    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            album.coverImg?.let { binding.itemAlbumCoverImgIv.setImageResource(it) }// 이 부분은 ai한테 물어봄, 홈 프래그먼트에 사진 바뀌는걸 뭐라고 줘야할까 몰랐음
        }
    }
}