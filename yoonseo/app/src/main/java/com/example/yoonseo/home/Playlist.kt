package com.example.yoonseo.home

data class Track(
    val title: String,
    val artist: String,
    val albumCover: Int // drawable 리소스 ID
)

data class Album(
    val title: String,
    val artist: String,
    val albumCover: Int,
    val tracks: List<Track>
)

data class Playlist(
    val title: String,
    val trackInfo: String,
    val tracks: List<Track>
)