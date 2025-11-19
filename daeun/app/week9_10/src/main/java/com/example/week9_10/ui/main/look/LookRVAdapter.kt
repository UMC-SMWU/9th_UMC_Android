package com.example.week9_10.ui.main.look

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week9_10.databinding.ItemLookThemeBinding

class LookRVAdapter(private val lookThemeList: ArrayList<String>): RecyclerView.Adapter<LookRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemLookThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = lookThemeList.size

    inner class ViewHolder(val binding: ItemLookThemeBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.lookThemeBtn.text = lookThemeList[position]
        }
    }
}