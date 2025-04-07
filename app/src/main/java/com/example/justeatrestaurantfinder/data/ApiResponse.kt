package com.example.justeatrestaurantfinder.data

import com.google.gson.annotations.SerializedName

// Simplified main structure based on typical API responses
// You might need to adjust this based on the *actual* structure
// of https://uk.api.just-eat.io/discovery/uk/restaurants/enriched/bypostcode/{postcode}
data class ApiResponse(
    @SerializedName("restaurants") // Assuming the key for the list is "restaurants"
    val restaurants: List<Restaurant>?
    // Add other top-level fields if needed, like metadata, errors etc.
    // @SerializedName("metaData") val metaData: MetaData?
)