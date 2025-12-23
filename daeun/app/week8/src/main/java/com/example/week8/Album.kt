package com.example.week8

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("AlbumTable")
data class Album(
    @PrimaryKey(false) var id:Int = 0,
    var title: String?= "",
    var singer: String? = "",
    var coverImg: Int? = null,
    var description: String? = "",
    var isPlaying: Boolean = false,
    //var songs:ArrayList<Song>? = null
)