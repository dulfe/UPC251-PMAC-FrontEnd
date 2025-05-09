package com.betondecken.trackingsystem.support

import com.betondecken.trackingsystem.entities.AccessToken
import com.betondecken.trackingsystem.entities.UsuarioResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private var _accessToken: AccessToken? = null
    private var _user: UsuarioResponse? = null

    fun setAccessToken(token: AccessToken) {
        _accessToken = token
    }

    fun getAccessToken(): AccessToken? {
        return _accessToken
    }

    fun getUser(): UsuarioResponse? {
        return _user
    }

    fun setUser(user: UsuarioResponse) {
        _user = user
    }

    fun clearSession() {
        _accessToken = null
    }
}