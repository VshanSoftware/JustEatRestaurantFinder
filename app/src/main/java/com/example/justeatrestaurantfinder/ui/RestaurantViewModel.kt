package com.example.justeatrestaurantfinder.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.justeatrestaurantfinder.R
import com.example.justeatrestaurantfinder.data.Restaurant
import com.example.justeatrestaurantfinder.repository.RestaurantRepository
import com.example.justeatrestaurantfinder.utils.ValidationUtils
import kotlinx.coroutines.launch

// States for the UI
enum class UiState { IDLE, LOADING, SUCCESS, ERROR, EMPTY }

class RestaurantViewModel : ViewModel() {

    private val repository = RestaurantRepository()

    // --- LiveData ---
    private val _restaurants = MutableLiveData<List<Restaurant>>()
    val restaurants: LiveData<List<Restaurant>> = _restaurants

    private val _uiState = MutableLiveData<UiState>(UiState.IDLE)
    val uiState: LiveData<UiState> = _uiState

    private val _message = MutableLiveData<Int?>() // String resource ID for messages
    val message: LiveData<Int?> = _message

    private val _messageArgs = MutableLiveData<Array<Any>?>(null) // Args for formatted messages
    val messageArgs: LiveData<Array<Any>?> = _messageArgs

    // --- Public Functions ---

    // Called by Activity to start search
    fun fetchRestaurants(postcode: String) {
        val trimmedPostcode = postcode.trim()

        // Basic input validation
        if (trimmedPostcode.isBlank()) {
            _message.value = R.string.enter_postcode_message
            _uiState.value = UiState.ERROR
            return
        }
        if (!ValidationUtils.isValidUkPostcode(trimmedPostcode)) {
            _message.value = R.string.invalid_postcode_format
            _uiState.value = UiState.ERROR
            return
        }

        // Looks okay, start loading sequence
        _uiState.value = UiState.LOADING
        _message.value = null
        _messageArgs.value = null

        // Perform network call in background
        viewModelScope.launch {
            val result = repository.getRestaurants(trimmedPostcode)

            // Handle success/failure from the repo
            result.onSuccess { apiResponse ->
                val validRestaurants = apiResponse.restaurants?.filterNotNull() ?: emptyList()
                val firstTen = validRestaurants.take(10) // Limit to 10 results
                _restaurants.value = firstTen

                // Update state based on whether we got results
                if (firstTen.isEmpty()) {
                    _uiState.value = UiState.EMPTY
                    _message.value = R.string.no_results_message
                } else {
                    _uiState.value = UiState.SUCCESS
                    _message.value = null
                }

            }.onFailure { exception ->
                // Handle errors
                _restaurants.value = emptyList()
                _uiState.value = UiState.ERROR
                _message.value = R.string.error_fetching_message
                _messageArgs.value = arrayOf(exception.message ?: "Unknown error") // Pass error detail
            }
        }
    }

    // UI should call this after showing a message
    fun clearMessage() {
        _message.value = null
        _messageArgs.value = null
    }
}