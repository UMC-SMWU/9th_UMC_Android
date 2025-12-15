package com.example.week9

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/signup")
    fun signUp(@Body user: User): Call<AuthResponse>

    @POST("/login")
    fun login(@Body user: User): Call<AuthResponse>

    @GET("/test") // API 명세에 따라 엔드포인트는 다를 수 있습니다.
    fun test(
        @Header("Authorization") accessToken: String
    ): Call<AuthResponse>

}