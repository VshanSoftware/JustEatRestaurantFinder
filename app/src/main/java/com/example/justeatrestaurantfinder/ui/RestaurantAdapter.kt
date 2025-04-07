package com.example.justeatrestaurantfinder.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.justeatrestaurantfinder.R
import com.example.justeatrestaurantfinder.data.Restaurant
import com.example.justeatrestaurantfinder.databinding.ListItemRestaurantBinding
import kotlin.math.floor

// RecyclerView Adapter using ListAdapter for better performance with DiffUtil
class RestaurantAdapter : ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder>(RestaurantDiffCallback()) {

    // Inflates the item layout and creates the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = ListItemRestaurantBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RestaurantViewHolder(binding)
    }

    // Binds data to the ViewHolder at the given position
    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Represents a single item view in the list
    inner class RestaurantViewHolder(private val binding: ListItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context // Handy for getting strings etc.

        // Update views in the list item layout with restaurant data
        fun bind(restaurant: Restaurant) {
            val naString = context.getString(R.string.data_not_available)

            // --- Bind data to views ---
            binding.textViewName.text = restaurant.name ?: naString

            val ratingValue = restaurant.rating?.starRating
            if (ratingValue != null) {
                binding.textViewRatingValue.text = context.getString(R.string.rating_format_string, ratingValue)
                binding.textViewRatingStars.text = getStarRatingString(ratingValue) // Show stars
            } else {
                binding.textViewRatingValue.text = naString
                binding.textViewRatingStars.text = ""
            }

            val cuisinesString = restaurant.cuisines
                ?.mapNotNull { it.name }
                ?.joinToString(", ")
                .takeIf { !it.isNullOrBlank() }
            binding.textViewCuisines.text = "${context.getString(R.string.cuisines_prefix)} ${cuisinesString ?: naString}"


            val addressString = restaurant.address?.getFormattedAddress()
                .takeIf { !it.isNullOrBlank() }
            binding.textViewAddress.text = "${context.getString(R.string.address_prefix)} ${addressString ?: naString}"
        }

        // Simple helper for star rating display
        private fun getStarRatingString(rating: Double, maxStars: Int = 5): String {
            val filledStars = floor(rating).toInt()
            val halfStar = if (rating - filledStars >= 0.5) 1 else 0 // Basic half-star logic
            val emptyStars = maxStars - filledStars - halfStar
            return "★".repeat(filledStars) + (if (halfStar == 1) "½" else "") + "☆".repeat(emptyStars)
        }
    }

    // --- DiffUtil Callback ---
    // Helps RecyclerView update list efficiently
    class RestaurantDiffCallback : DiffUtil.ItemCallback<Restaurant>() {
        // Check if items represent the same entity (e.g., using a unique ID if available)
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            // Using name+address as a proxy for unique ID here
            return oldItem.name == newItem.name && oldItem.address?.getFormattedAddress() == newItem.address?.getFormattedAddress()
        }

        // Check if the contents of the items are the same
        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            // Data class '==' checks all properties
            return oldItem == newItem
        }
    }
}