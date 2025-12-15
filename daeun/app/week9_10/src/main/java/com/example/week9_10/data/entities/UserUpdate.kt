package com.example.week9_10.data.entities

import com.google.gson.annotations.SerializedName

data class UserUpdate(
    @SerializedName("memberId") var id: Int = 0,
    @SerializedName("newName") var name: String,
    @SerializedName("newPassword") var pwd: String
)