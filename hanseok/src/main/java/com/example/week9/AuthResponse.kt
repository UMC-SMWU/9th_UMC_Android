package com.example.week9

import com.google.gson.annotations.SerializedName

// 이 부분은 그대로 둡니다.
data class AuthResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: AuthResult?
)

data class AuthResult(
    @SerializedName("memberId") val memberId: Int,
    //로그인에만 사용
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("refreshToken") val refreshToken: String?
)
