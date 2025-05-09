package com.betondecken.trackingsystem.datasources.services.infrastructure

import com.betondecken.trackingsystem.support.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val accessTokenManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Verifica si el endpoint actual requiere autenticación (puedes usar diferentes estrategias)
        if (shouldAddAuthorizationHeader(originalRequest)) {
            val accessToken = accessTokenManager.getAccessToken()?.access_token
            if (!accessToken.isNullOrBlank()) {
                // Construye una nueva Request con el header de Authorization
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
                return chain.proceed(newRequest)
            }
        }

        // Si no se requiere autenticación o no hay token, procede con la petición original
        return chain.proceed(originalRequest)
    }

    private fun shouldAddAuthorizationHeader(request: okhttp3.Request): Boolean {
        val path = request.url.encodedPath

        // Lista de rutas que NO requieren autorización
        val excludedPaths = listOf(
            "/api/Usuarios/registrar",
            "/api/Usuarios/login",
            "/api/Usuarios/refresh"
        )

        // Si la ruta de la petición NO está en la lista de exclusiones, añadimos el header
        return !excludedPaths.contains(path)
    }
}
