package com.example.justeatrestaurantfinder.repository

import com.example.justeatrestaurantfinder.data.ApiResponse
import com.example.justeatrestaurantfinder.network.ApiService
import com.example.justeatrestaurantfinder.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Handles getting data, usually from network or db (just network here)
class RestaurantRepository(private val apiService: ApiService = RetrofitInstance.api) {

    // Fetches restaurants for a given postcode
    suspend fun getRestaurants(postcode: String): Result<ApiResponse> {
        // Run blocking network call on background thread
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getRestaurantsByPostcode(postcode)
                // Check API call success
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!) // All good
                } else {
                    // API returned an error code
                    Result.failure(Exception("API Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                // Network issue probably
                Result.failure(e)
            }
        }
    }
}
// Using Kotlin's Result wrapper is cleaner than throwing exceptions everywhere