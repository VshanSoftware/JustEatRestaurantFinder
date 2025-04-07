package com.example.justeatrestaurantfinder.data

import com.google.gson.annotations.SerializedName

data class Restaurant(
    @SerializedName("name")
    val name: String?,

    @SerializedName("cuisines")
    val cuisines: List<Cuisine>?,

    @SerializedName("rating") // Assuming the rating object is nested under "rating"
    val rating: Rating?,

    @SerializedName("address") // Assuming the address object is nested under "address"
    val address: Address?
)