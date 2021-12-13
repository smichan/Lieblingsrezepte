package com.example.sophieslieblingsrezepte.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val apiToken: String,
    val displayName: String
)