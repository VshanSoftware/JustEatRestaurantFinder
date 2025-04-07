package com.example.justeatrestaurantfinder

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justeatrestaurantfinder.databinding.ActivityMainBinding
import com.example.justeatrestaurantfinder.ui.RestaurantAdapter
import com.example.justeatrestaurantfinder.ui.RestaurantViewModel
import com.example.justeatrestaurantfinder.ui.UiState

// Main screen of the app
class MainActivity : AppCompatActivity() {

    // Using view binding and viewModels delegate
    private lateinit var binding: ActivityMainBinding
    private val viewModel: RestaurantViewModel by viewModels()
    private lateinit var restaurantAdapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup UI components and observers
        setupRecyclerView()
        setupObservers()
        setupListeners()

        // Set initial message if nothing's loaded
        if (viewModel.uiState.value == UiState.IDLE) {
            binding.textViewInfo.setText(R.string.initial_prompt_message)
            binding.textViewInfo.visibility = View.VISIBLE
        }
    }

    // Configure the RecyclerView
    private fun setupRecyclerView() {
        restaurantAdapter = RestaurantAdapter()
        binding.recyclerViewRestaurants.apply {
            adapter = restaurantAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    // Set up listeners for button clicks, keyboard actions etc.
    private fun setupListeners() {
        binding.buttonSearch.setOnClickListener {
            performSearch()
        }

        // Handle search action from the keyboard
        binding.editTextPostcode.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@OnEditorActionListener true // Consume the event
            }
            false
        })
    }

    // Trigger the search in the ViewModel
    private fun performSearch() {
        val postcode = binding.editTextPostcode.text.toString()
        viewModel.fetchRestaurants(postcode)
        hideKeyboard()
    }

    // Observe LiveData from ViewModel and update UI accordingly
    private fun setupObservers() {
        // Update list when restaurant data changes
        viewModel.restaurants.observe(this) { restaurants ->
            // submitList uses DiffUtil for efficient updates
            restaurantAdapter.submitList(restaurants)
        }

        // Update overall UI visibility based on state changes
        viewModel.uiState.observe(this) { state ->
            updateUiVisibility(state ?: UiState.IDLE) // Default to IDLE if state is null initially
        }

        // Show messages (from validation, errors, empty state)
        viewModel.message.observe(this) { messageResId ->
            messageResId?.let {
                val messageArgs = viewModel.messageArgs.value
                // Format message if arguments are provided
                val message = if (messageArgs != null) getString(it, *messageArgs) else getString(it)

                // Show important state messages in the TextView, others maybe as Toast?
                // (Current logic puts ERROR/EMPTY/IDLE messages in TextViewInfo)
                if (viewModel.uiState.value == UiState.ERROR || viewModel.uiState.value == UiState.EMPTY || viewModel.uiState.value == UiState.IDLE) {
                    binding.textViewInfo.text = message
                } else {
                    // Could potentially show transient messages (e.g., success confirmations?) here
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
                viewModel.clearMessage() // Reset message after showing
            }
        }
    }

    // Manage visibility of ProgressBar, RecyclerView, and Info TextView based on UiState
    private fun updateUiVisibility(state: UiState) {
        binding.progressBar.visibility = if (state == UiState.LOADING) View.VISIBLE else View.GONE
        binding.recyclerViewRestaurants.visibility = if (state == UiState.SUCCESS) View.VISIBLE else View.GONE
        binding.textViewInfo.visibility = if (state == UiState.ERROR || state == UiState.EMPTY || state == UiState.IDLE) View.VISIBLE else View.GONE

        // Prevent user interaction while loading
        binding.buttonSearch.isEnabled = state != UiState.LOADING
        binding.editTextPostcode.isEnabled = state != UiState.LOADING

        // Set default text for info view if no specific message is set for the current state
        if (binding.textViewInfo.visibility == View.VISIBLE && viewModel.message.value == null) {
            binding.textViewInfo.text = when (state) {
                UiState.IDLE -> getString(R.string.initial_prompt_message)
                UiState.EMPTY -> getString(R.string.no_results_message)
                else -> "" // Keep existing text (e.g., error message already set)
            }
        }
    }

    // Utility to hide the soft keyboard
    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        currentFocus?.let { // Hide based on focused view if possible
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        } ?: run { // Fallback if no view has focus
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
    }
}