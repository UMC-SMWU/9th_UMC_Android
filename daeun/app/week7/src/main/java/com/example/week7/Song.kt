package com.example.week7

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.SET_DEFAULT
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable",
    foreignKeys = [ForeignKey(
        Album::class, ["albumId"],
        ["albumIdx"], SET_DEFAULT, SET_DEFAULT, false)])
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
