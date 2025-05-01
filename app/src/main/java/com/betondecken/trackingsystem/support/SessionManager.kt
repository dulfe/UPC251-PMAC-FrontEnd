package com.betondecken.trackingsystem.support

import com.betondecken.trackingsystem.entities.AccessToken
import javax.inject.Inject

class SessionManager @Inject constructor() {
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