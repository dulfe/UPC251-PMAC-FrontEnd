package com.betondecken.trackingsystem.datasources

import com.betondecken.trackingsystem.entities.AccessToken
import com.betondecken.trackingsystem.entities.DataSourceResult
import com.betondecken.trackingsystem.entities.SimpleError
import com.betondecken.trackingsystem.entities.Usuario
import java.time.OffsetDateTime


class UserDataSource(
) {
    suspend fun login(username: String, password: String): DataSourceResult<AccessToken> {
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
        val token = AccessToken(
            access_token = "mock_access",
            scope = "mock_scope",
            token_type = "mock_type",
            refresh_token = "fake_refresh",
            expires_in = 3600
        )
        return DataSourceResult.Success(token)
    }

    suspend fun whoAmI(): DataSourceResult<Usuario>  {
        val result = Usuario(
            email = "test@server.com",
            estado = "A",
            nombres = "Test",
            apellidos = "User",
            usuarioId = 1,
            passwordHash = "XYZ",
            fechaDeCreacion = OffsetDateTime.now()
        )

        return DataSourceResult.Success(result)
    }
}