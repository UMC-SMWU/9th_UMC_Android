package com.example.yoonseo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LikeTable")
class Like(
    val userId: Int,
    val albumId: Int,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}