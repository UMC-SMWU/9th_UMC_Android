package com.example.week8

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("LikeTable")
data class Like(
    var userId: Int,
    var albumId: Int
){
    @PrimaryKey(true) var id: Int = 0
}
