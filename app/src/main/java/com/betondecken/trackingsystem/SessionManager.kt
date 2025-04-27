package com.betondecken.trackingsystem

import com.betondecken.trackingsystem.entities.AccessToken

class SessionManager {
    private var _accessToken: AccessToken? = null

    fun saveAccessToken(token: AccessToken) {
        _accessToken = token
    }
    fun getAccessToken(): AccessToken? {
        return _accessToken
    }
    fun clearSession() {
        _accessToken = null
    }
}