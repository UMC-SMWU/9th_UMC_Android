package com.example.week9_10.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.week9_10.R

@Entity("AlbumTable")
data class Album(
    @PrimaryKey @ColumnInfo(name = "id") var id:Int? = 0,
    @ColumnInfo("title") var title: String = "",
    @ColumnInfo("singer") var singer: String = "",
    @ColumnInfo("coverImg") var coverImg: Int = R.drawable.img_album_exp,
    @ColumnInfo("description") var description: String = "",
    @ColumnInfo("isPlaying") var isPlaying: Boolean = false
)