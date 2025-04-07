package com.example.justeatrestaurantfinder.data

import com.google.gson.annotations.SerializedName

// Represents the address part of a restaurant
data class Address(
    @SerializedName("firstLine")
    val firstLine: String?,

    @SerializedName("city")
    val city: String?,

    // Field name from API might be different, check actual JSON if issues arise
    @SerializedName("postalCode")
    val postcode: String?
) {
    // Simple helper to combine address parts into one string
    fun getFormattedAddress(): String {
        // joinToString handles nulls nicely
        return listOfNotNull(firstLine, city, postcode).joinToString(", ")
    }
}