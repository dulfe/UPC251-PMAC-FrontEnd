package com.betondecken.trackingsystem.datasources

import com.betondecken.trackingsystem.datasources.entities.AccessToken

class UserDataSource(
) {
    suspend fun login(username: String, password: String): AccessToken {
        return AccessToken(
            access_token = "mock_access",
            scope = "mock_scope",
            token_type = "mock_type",
            refresh_token = "fake_refresh",
            expires_in = 3600
        )
    }
}