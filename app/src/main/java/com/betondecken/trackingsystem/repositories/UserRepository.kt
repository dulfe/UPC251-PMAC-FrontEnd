package com.betondecken.trackingsystem.repositories

import com.betondecken.trackingsystem.SessionManager
import com.betondecken.trackingsystem.datasources.UserDataSource
import com.betondecken.trackingsystem.entities.AccessToken
import com.betondecken.trackingsystem.entities.DataSourceResult
import com.betondecken.trackingsystem.entities.SimpleError
import com.betondecken.trackingsystem.entities.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

sealed class UserLoginResult {
    data class Success(val user: Usuario) : UserLoginResult()
    data class Error(val message: String) : UserLoginResult()
    //data object NotFound : UserLoginResult()
    //data object InvalidCredentials : UserLoginResult()
}

class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource,
    private val sessionManager: SessionManager
) {

    // TODO: No creo que necesito esto
    private val _user = MutableStateFlow<Usuario?>(null)
    val user: StateFlow<Usuario?> = _user

    suspend fun login(username: String, password: String): UserLoginResult {
        sessionManager.clearSession()

        val accessToken = userDataSource.login(username, password)
        if (accessToken is DataSourceResult.Success) {
            sessionManager.saveAccessToken(accessToken.data)

            val user = userDataSource.whoAmI()
            if (user is DataSourceResult.Success) {
                _user.value = user.data

                return UserLoginResult.Success(user.data)
            }
        }
        return UserLoginResult.Error("Error al iniciar sesión")
    }
}