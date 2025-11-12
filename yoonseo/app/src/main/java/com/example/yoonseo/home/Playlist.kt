package com.example.yoonseo.home

data class Album(
    val title: String,
    val artist: String,
    val albumCover: Int,
    val tracks: List<Track>
)

data class Track(
    val trackNumber: Int,
    val title: String,
    val artist: String,
    val albumCover: Int
)

data class Playlist(
    val title: String,
    val trackInfo: String,
    val tracks: List<Track>
)