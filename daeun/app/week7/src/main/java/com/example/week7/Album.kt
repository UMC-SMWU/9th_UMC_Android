package com.example.week7

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("AlbumTable")
data class Album(
    var title: String?= "",
    var singer: String? = "",
    var coverImg: Int? = null,
    var description: String? = "",
    var isPlaying: Boolean = false,
    //var songs:ArrayList<Song>? = null
){
    @PrimaryKey(true) var albumId:Int = 0
}