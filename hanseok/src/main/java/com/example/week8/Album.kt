package com.example.week8


data class Album(
    var id: String = "",
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
    var albumIdx: Int = 0,
    var songs: ArrayList<Song>? = null
)
