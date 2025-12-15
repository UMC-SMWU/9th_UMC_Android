package com.example.week9

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName(value = "email") var email: String = "",
    @SerializedName(value = "password") var password: String = "",
    @SerializedName(value = "name") var name: String = ""
)
