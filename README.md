# Early Careers Mobile Engineering Program (Android) - Complete at home Coding Assignment

This Android application allows users to enter a UK postcode and view a list of the first 10 Just Eat restaurants found in that area. It fetches data live from the Just Eat discovery API.

## How to Build, Compile, and Run

1.  **Clone the Repository:**
    ```bash
    git clone <https://github.com/VshanSoftware/JustEatRestaurantFinder.git>
    cd justeatrestaurantfinder
    ```
2.  **Open in Android Studio:**
    * Launch Android Studio.
    * Select "Open".
    * Navigate to and select the cloned `justeatrestaurantfinder` project directory.
    * Allow Android Studio to perform Gradle sync and download dependencies.
3.  **Run the App:**
    * Connect an Android device or start an Android Virtual Device.
    * Select the target device from the run configuration dropdown menu in Android Studio's toolbar.
    * Click the "Run 'app'" button or use the menu: `Run` > `Run 'app'`.
4.  **Usage:**
    * Enter a valid UK postcode (e.g., `EC4M 7RF`, `SW1A 0AA`) into the input field.
    * Tap the "Search" button or use the search action on your keyboard.
    * A progress bar indicates loading.
    * Results (up to 10 restaurants) are displayed in a list.
    * Status messages for errors, empty results, or invalid input format will appear in the text view below the search bar.

## Assumptions

* **API Response Structure:** The exact JSON field names within the API response were assumed based on the assignment requirements. Key assumptions include:
    * `restaurants` (List<Restaurant>)
    * `name` (String)
    * `cuisines` (List<Cuisine>) with `name` (String) inside each Cuisine object.
    * `rating` (Object) containing `starRating` (Double).
    * `address` (Object) containing `firstLine` (String), `city` (String), and `postalCode` (String).
    * If the actual API response differs, the Gson `@SerializedName` annotations in the `data` package classes would need adjustment.
* **API Availability & Authentication:** Assumed the API endpoint is publicly accessible without authentication headers or API keys.
* **UK Postcodes Only:** Relied on the API documentation stating it only works for UK postcodes. The implemented validation checks the format but doesn't guarantee the postcode exists or is geographically valid within the UK.

## Potential Improvements / Future Work

If I had more time, I would consider:

* **Testing:** Adding Unit Tests (ViewModel, Utils, Repository) and potentially Espresso UI tests.
* **Dependency Injection:** Implementing Hilt or Koin for better code structure and testability.
* **Error Handling:** Displaying network/API errors in a more user-friendly way (e.g., a dedicated error view with a retry button) instead of just updating the status text.
* **UI Enhancements:** Make the UI more engaging and visually appealing.
* **Image Loading:** If the API provided restaurant logos/images, integrating an image loading library like Coil or Glide.
* **Caching:** Implementing basic caching (in-memory or database) to avoid repeated API calls for the same postcode during a session.
* **Advanced Validation:** Using a more robust postcode validation library or method if higher accuracy was required.
