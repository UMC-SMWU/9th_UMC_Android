package com.example.week9_10.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://43.200.73.115:8080"

fun getRetrofit(): Retrofit{
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit
}