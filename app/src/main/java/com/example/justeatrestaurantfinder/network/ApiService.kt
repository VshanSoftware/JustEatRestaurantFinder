package com.example.justeatrestaurantfinder.network

import com.example.justeatrestaurantfinder.data.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

// Retrofit interface defining API endpoints
interface ApiService {

    // Get restaurants by postcode endpoint
    @Headers("Accept: application/json") // Specify we want JSON
    @GET("discovery/uk/restaurants/enriched/bypostcode/{postcode}")
    suspend fun getRestaurantsByPostcode(
        @Path("postcode") postcode: String // Injects postcode into the URL path
    ): Response<ApiResponse> // Coroutine suspend function returning Retrofit Response
}