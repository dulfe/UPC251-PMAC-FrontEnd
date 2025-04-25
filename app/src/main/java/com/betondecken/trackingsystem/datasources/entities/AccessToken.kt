package com.betondecken.trackingsystem.datasources.entities

data class AccessToken(
    val access_token: String,
    val refresh_token: String,
    val token_type: String,
    val scope: String,
    val expires_in: Int
)
