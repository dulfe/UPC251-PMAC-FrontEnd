package com.betondecken.trackingsystem.datasources

import com.betondecken.trackingsystem.entities.AccessToken
import com.betondecken.trackingsystem.entities.DataSourceResult
import com.betondecken.trackingsystem.entities.NuevoUsuarioInput
import com.betondecken.trackingsystem.entities.UsuarioResponse
import kotlinx.coroutines.delay
import java.time.OffsetDateTime
import javax.inject.Inject


class UserDataSource @Inject constructor (
) {
    suspend fun login(username: String, password: String): DataSourceResult<AccessToken> {
        // Simular una llamada a la API con un retraso
        delay(2000)

        // Retornar un ejemplo
        val token = AccessToken(
            access_token = "mock_access",
            scope = "mock_scope",
            token_type = "mock_type",
            refresh_token = "fake_refresh",
            expires_in = 3600
        )
        return DataSourceResult.Success(token)
    }

    suspend fun refresh(refreshToken: String): DataSourceResult<AccessToken>  {
        // Simular una llamada a la API con un retraso
        delay(2000)

        // Retornar un ejemplo
        val token = AccessToken(
            access_token = "mock_access",
            scope = "mock_scope",
            token_type = "mock_type",
            refresh_token = "fake_refresh",
            expires_in = 3600
        )
        return DataSourceResult.Success(token)
    }

    suspend fun whoAmI(): DataSourceResult<UsuarioResponse>  {
        // Simular una llamada a la API con un retraso
        delay(2000)

        // Retornar un ejemplo
        val result = UsuarioResponse(
            email = "test@server.com",
            estado = "A",
            nombres = "Test",
            apellidos = "User",
            usuarioId = 1,
            fechaDeCreacion = OffsetDateTime.now()
        )

        return DataSourceResult.Success(result)
    }

    suspend fun register(user: NuevoUsuarioInput): DataSourceResult<UsuarioResponse> {
        // Simular una llamada a la API con un retraso
        delay(2000)

        // Retornar un ejemplo
        val result = UsuarioResponse(
            email = user.email,
            estado = "A",
            nombres = user.nombres,
            apellidos = user.apellidos,
            usuarioId = 1,
            fechaDeCreacion = OffsetDateTime.now()
        )

        return DataSourceResult.Success(result)
    }
}