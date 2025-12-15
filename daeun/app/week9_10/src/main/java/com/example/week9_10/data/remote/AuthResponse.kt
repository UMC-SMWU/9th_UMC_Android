package com.example.week9_10.data.remote

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Data?,
)
data class Data(
    @SerializedName("name") val name:String = "",
    @SerializedName("code") val code:String ="",
    @SerializedName("memberId") val memberId: Int = 0,
    @SerializedName("accessToken") val accessToken:String="",
    @SerializedName("result") val result:String=""
)