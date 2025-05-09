package com.betondecken.trackingsystem.repositories

import com.betondecken.trackingsystem.datasources.UserDataSource
import com.betondecken.trackingsystem.entities.AccessToken
import com.betondecken.trackingsystem.entities.DataSourceResult
import com.betondecken.trackingsystem.entities.NuevoUsuarioInput
import com.betondecken.trackingsystem.entities.UsuarioResponse
import com.betondecken.trackingsystem.entities.RepositoryResult
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource
) {
    suspend fun login(username: String, password: String): RepositoryResult<AccessToken> {
        return when (val response = userDataSource.login(username, password)) {
            is DataSourceResult.Success -> {
                RepositoryResult.Success(response.data)
            }

            is DataSourceResult.Error -> {
                RepositoryResult.Error(response.error.mensaje)
            }
        }
    }

    suspend fun whoAmI(): RepositoryResult<UsuarioResponse> {
        return when (val result = userDataSource.whoAmI()) {
            is DataSourceResult.Success -> {
                RepositoryResult.Success(result.data)
            }

            is DataSourceResult.Error -> {
                RepositoryResult.Error(result.error.mensaje)
            }
        }
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