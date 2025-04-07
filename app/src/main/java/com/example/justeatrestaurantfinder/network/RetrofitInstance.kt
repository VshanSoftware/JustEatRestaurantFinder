package com.example.justeatrestaurantfinder.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton for setting up Retrofit once
object RetrofitInstance {

    private const val BASE_URL = "https://uk.api.just-eat.io/"

    // Lazy init: create Retrofit only when needed the first time
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // For JSON parsing
            .build()
            .create(ApiService::class.java)
    }
}