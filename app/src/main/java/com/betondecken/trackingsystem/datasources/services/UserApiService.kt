package com.betondecken.trackingsystem.datasources.services

import com.betondecken.trackingsystem.entities.AccessToken
import com.betondecken.trackingsystem.entities.UsuarioResponse
import com.betondecken.trackingsystem.entities.VerificarCredencialesInput
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {
    @POST("api/Usuarios/login")
    suspend fun login(@Body payload: VerificarCredencialesInput): Response<AccessToken>

    @GET("api/Usuarios/whoami")
    suspend fun whoAmI(): Response<UsuarioResponse>
}