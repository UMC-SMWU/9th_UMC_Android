package com.example.week8

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class User(
    var email: String,
    var password: String,
){
    @PrimaryKey(true) var id: Int = 0
}