package com.example.week9_10.data.remote

import com.example.week9_10.data.entities.User
import com.example.week9_10.data.entities.UserUpdate
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthRetrofitInterface {
    @POST("/signup")
    fun signUp(@Body user: User): Call<AuthResponse>
    @POST("/login")
    fun login(@Body user: User): Call<AuthResponse>
    @GET("/test")
    fun test(@Header("Authorization") token: String): Call<AuthResponse>
    @PATCH("/users")
    fun update(@Header("Authorization") token: String, @Body user: UserUpdate): Call<AuthResponse>
    @DELETE("/users")
    fun delete(@Header("Authorization") token:String,
               @Query("memberId") memberId: Int,
               @Query("password") password: String
    )
    : Call<AuthResponse>
}