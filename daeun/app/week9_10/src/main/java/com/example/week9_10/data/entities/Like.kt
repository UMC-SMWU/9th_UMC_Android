package com.example.week9_10.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("LikeTable")
data class Like(
    @ColumnInfo("userId") var userId: Int,
    @ColumnInfo("albumId") var albumId: Int?
){
    @PrimaryKey(true) var id: Int = 0
}
