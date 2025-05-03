package com.betondecken.trackingsystem.datasources

import com.betondecken.trackingsystem.datasources.services.UserApiService
import com.betondecken.trackingsystem.datasources.services.infrastructure.processResponse
import com.betondecken.trackingsystem.entities.AccessToken
import com.betondecken.trackingsystem.entities.DataSourceResult
import com.betondecken.trackingsystem.entities.NuevoUsuarioInput
import com.betondecken.trackingsystem.entities.SimpleError
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

    suspend fun whoAmI(): DataSourceResult<UsuarioResponse> {
        val response = userApiService.whoAmI()

        return response.processResponse(retrofit)

//        // Simular una llamada a la API con un retraso
//        delay(2000)
//
//        // Retornar un ejemplo
//        val result = UsuarioResponse(
//            email = "test@server.com",
//            estado = "A",
//            nombres = "Test",
//            apellidos = "User",
//            usuarioId = 1,
//            fechaDeCreacion = OffsetDateTime.now()
//        )
//
//        return DataSourceResult.Success(result)
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

//    // Función de ejemplo para parsear el cuerpo de error (ajústala según tu API)
//    private fun parseError(code: Int, errorBody: String?): SimpleError {
//        // Aquí podrías usar Gson o Moshi para deserializar el errorBody
//        // a un objeto de error específico de tu API si es necesario.
//        // Por ahora, creamos un SimpleError genérico basado en el código de respuesta.
//        return SimpleError(code.toString(), errorBody ?: "Error HTTP $code")
//    }
}