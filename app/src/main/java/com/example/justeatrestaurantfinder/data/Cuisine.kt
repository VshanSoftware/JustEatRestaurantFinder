package com.example.justeatrestaurantfinder.data

import com.google.gson.annotations.SerializedName

data class Cuisine(
    @SerializedName("name")
    val name: String?
    // Add other fields if available/needed, e.g., "id"
)