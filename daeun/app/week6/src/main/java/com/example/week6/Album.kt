package com.example.week6

data class Album(
    var title: String?= "",
    var singer: String? = "",
    var coverImg: Int? = null,
    var description: String? = "",
    var isPlaying: Boolean = false,
    var songs:ArrayList<Song>? = null
)