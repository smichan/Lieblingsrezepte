package com.example.sophieslieblingsrezepte.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val token: String
    //... other data fields that may be accessible to the UI
)