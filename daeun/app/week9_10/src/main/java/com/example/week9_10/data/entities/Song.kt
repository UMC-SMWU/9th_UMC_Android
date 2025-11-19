package com.example.week9_10.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.week9_10.R

@Entity(tableName = "SongTable")
data class Song(
    val title:String? = "",
    val singer:String? = "",
    var second:Int = 0,
    val playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String? = "",
    var albumCover: Int = R.drawable.img_album_exp,
    var isLike:Boolean = false,
    var albumIdx: Int = 0
){
    @PrimaryKey(true) var id: Int = 0
}
