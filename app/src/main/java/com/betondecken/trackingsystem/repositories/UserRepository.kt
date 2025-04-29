package com.betondecken.trackingsystem.repositories

import com.betondecken.trackingsystem.SessionManager
import com.betondecken.trackingsystem.datasources.UserDataSource
import com.betondecken.trackingsystem.entities.DataSourceResult
import com.betondecken.trackingsystem.entities.NuevoUsuarioInput
import com.betondecken.trackingsystem.entities.UsuarioResponse
import com.betondecken.trackingsystem.entities.RepositoryResult
import javax.inject.Inject

sealed class UserLoginResult {
    data class Success(val user: UsuarioResponse) : UserLoginResult()
    data class Error(val message: String) : UserLoginResult()
    //data object NotFound : UserLoginResult()
    //data object InvalidCredentials : UserLoginResult()
}

class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource,
    private val sessionManager: SessionManager
) {

//    // TODO: No creo que necesito esto
//    private val _user = MutableStateFlow<UsuarioResponse?>(null)
//    val user: StateFlow<UsuarioResponse?> = _user

    suspend fun login(username: String, password: String): UserLoginResult {
        sessionManager.clearSession()

        val accessToken = userDataSource.login(username, password)
        if (accessToken is DataSourceResult.Success) {
            sessionManager.saveAccessToken(accessToken.data)

            val user = userDataSource.whoAmI()
            if (user is DataSourceResult.Success) {
                //_user.value = user.data

                return UserLoginResult.Success(user.data)
            }
        }
        return UserLoginResult.Error("Error al iniciar sesión")
    }

    suspend fun register(user: NuevoUsuarioInput): RepositoryResult<UsuarioResponse> {
        val result = userDataSource.register(user)
        if (result is DataSourceResult.Success) {
            return RepositoryResult.Success(result.data)
        } else if (result is DataSourceResult.Error) {
            return RepositoryResult.Error(result.error.mensaje)
        }
        return RepositoryResult.Error("Error al registrar el usuario")
    }
}