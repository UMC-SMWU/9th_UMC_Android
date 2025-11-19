package com.example.week8

data class Song(
    var id: String = "",
    var title: String = "",
    var singer: String = "",
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
    var coverImg: Int? = null,
    @JvmField var isLike: Boolean = false,
    var albumIdx: Int = 0
)


