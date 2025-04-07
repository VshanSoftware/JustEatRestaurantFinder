package com.example.justeatrestaurantfinder.utils

import java.util.regex.Pattern

// Quick postcode validation helper
object ValidationUtils {

    // UK Postcode Regex - found online, seems to cover the main formats
    private val UK_POSTCODE_REGEX = Pattern.compile(
        "^[A-Z]{1,2}[0-9R][0-9A-Z]?\\s?[0-9][A-Z]{2}$",
        Pattern.CASE_INSENSITIVE
    )

    fun isValidUkPostcode(postcode: String): Boolean {
        // Basic check against the regex pattern
        return UK_POSTCODE_REGEX.matcher(postcode.trim()).matches()
    }
}