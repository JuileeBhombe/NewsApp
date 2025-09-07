package com.juileebhombe.newsapp.util

object Constants {

    object Strings {

        // Error Messages
        const val API_ERROR_PREFIX = "API Error: "
        const val NETWORK_ERROR_PREFIX = "Network error: "
        const val NETWORK_CONNECTION_ERROR = "Network connection error. Please check your internet connection."
        const val UNEXPECTED_ERROR_PREFIX = "Unexpected error: "
        const val NO_INTERNET_CONNECTION = "No internet connection. Please check your network and try again."
        const val FAILED_TO_LOAD_NEWS = "Failed to load news"
        const val UNKNOWN_ERROR_OCCURRED = "Unknown error occurred"
        const val COULD_NOT_LOAD_NEWS = "Could not load news. Check your internet and try again."

        // UI Text Strings
        const val NEWS_HEADLINES = "News Headlines"
        const val SEE_FULL_ARTICLE = "See Full Article"
        const val BY_AUTHOR_PREFIX = "By "
        const val DESCRIPTION = "Description"
        const val CONTENT = "Content"
        const val LOADING_NEWS = "Loading news..."
        const val NO_NEWS_FOUND = "No news found."
        const val RETRY = "Retry"
        const val JUST_NOW = "Just now"

        // Content Descriptions
        const val BACK_BUTTON_DESCRIPTION = "Back"
        const val ARTICLE_IMAGE_DESCRIPTION = "Article image"
        const val ARTICLE_HEADER_IMAGE_DESCRIPTION = "Article header image"

        // Navigation Routes
        const val NEWS_LIST_ROUTE = "news_list"
        const val ARTICLE_DETAIL_ROUTE = "article_detail"
    }
}
