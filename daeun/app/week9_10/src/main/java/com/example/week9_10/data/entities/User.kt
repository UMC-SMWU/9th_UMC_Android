package com.example.week9_10.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "UserTable")
data class User(
    //@SerializedName(value): 서버와 같은 이름 사용
    @PrimaryKey @SerializedName("memberId") var id: Int = 0,
    @SerializedName("name") var name: String="",
    @SerializedName("email") var email: String,
    @SerializedName("password")var password: String,
)