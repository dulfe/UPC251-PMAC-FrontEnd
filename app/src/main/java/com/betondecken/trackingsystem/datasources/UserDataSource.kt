package com.betondecken.trackingsystem.datasources

import com.betondecken.trackingsystem.datasources.services.UserApiService
import com.betondecken.trackingsystem.datasources.services.infrastructure.processResponse
import com.betondecken.trackingsystem.entities.AccessToken
import com.betondecken.trackingsystem.entities.DataSourceResult
import com.betondecken.trackingsystem.entities.NuevoUsuarioInput
import com.betondecken.trackingsystem.entities.SimpleError
import com.betondecken.trackingsystem.entities.TokenRefreshRequestInput
import com.betondecken.trackingsystem.entities.UsuarioResponse
import com.betondecken.trackingsystem.entities.VerificarCredencialesInput
import kotlinx.coroutines.delay
import retrofit2.Response
import retrofit2.Retrofit
import java.time.OffsetDateTime
import javax.inject.Inject


class UserDataSource @Inject constructor(
    private val retrofit: Retrofit,
    private val userApiService: UserApiService
) {
    suspend fun login(username: String, password: String): DataSourceResult<AccessToken> {
        val response: Response<AccessToken> =
            userApiService.login(VerificarCredencialesInput(username, password))
        return response.processResponse(retrofit)
    }

    suspend fun refresh(refreshToken: String): DataSourceResult<AccessToken> {
        val response: Response<AccessToken> =
            userApiService.refresh(TokenRefreshRequestInput(refreshToken))
        return response.processResponse(retrofit)
    }

    suspend fun whoAmI(): DataSourceResult<UsuarioResponse> {
        val response = userApiService.whoAmI()
        return response.processResponse(retrofit)
    }

    suspend fun register(user: NuevoUsuarioInput): DataSourceResult<UsuarioResponse> {
        val response = userApiService.register(user)
        return response.processResponse(retrofit)
    }
}