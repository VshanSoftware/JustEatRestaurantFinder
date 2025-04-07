package com.example.justeatrestaurantfinder.data

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("starRating") // Focusing on the numeric rating [cite: 3, 7]
    val starRating: Double?
    // Add other fields if available/needed, e.g., "count"
)