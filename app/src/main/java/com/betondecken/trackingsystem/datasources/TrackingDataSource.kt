package com.betondecken.trackingsystem.datasources

import com.betondecken.trackingsystem.datasources.services.TrackingApiService
import com.betondecken.trackingsystem.datasources.services.UserApiService
import com.betondecken.trackingsystem.datasources.services.infrastructure.processResponse
import com.betondecken.trackingsystem.entities.AccessToken
import com.betondecken.trackingsystem.entities.DataSourceResult
import com.betondecken.trackingsystem.entities.DetalleDeOrdenResponse
import com.betondecken.trackingsystem.entities.RastreoEnTiempoRealResponse
import com.betondecken.trackingsystem.entities.RepositoryResult
import com.betondecken.trackingsystem.entities.ResumenDeOrdenResponse
import com.betondecken.trackingsystem.entities.VerificarCredencialesInput
import javax.inject.Inject
import kotlinx.coroutines.delay
import retrofit2.Response
import retrofit2.Retrofit
import java.time.OffsetDateTime

class TrackingDataSource @Inject constructor(
    private val retrofit: Retrofit,
    private val trackingApiService: TrackingApiService
) {
    suspend fun getOrdenes(cantidad: Int): DataSourceResult<List<ResumenDeOrdenResponse>> {
        val response: Response<List<ResumenDeOrdenResponse>> =
            trackingApiService.getOrdenes(cantidad)

        return response.processResponse(retrofit)
    }

    suspend fun getOrdenPorCodigoDeSeguimiento(codigoDeSeguimiento: String): DataSourceResult<DetalleDeOrdenResponse> {
        val response: Response<DetalleDeOrdenResponse> =
            trackingApiService.getOrdenPorCodigoDeSeguimiento(codigoDeSeguimiento)

        return response.processResponse(retrofit)
    }

    suspend fun deleteOrdenPorUsuario(codigoDeSeguimiento: String): DataSourceResult<Unit> {
        val response: Response<Unit> =
            trackingApiService.deleteOrdenPorUsuario(codigoDeSeguimiento)

        return response.processResponse(retrofit)
    }

    suspend fun getUbicacionActual(codigoDeSeguimiento: String): DataSourceResult<RastreoEnTiempoRealResponse> {
        val response: Response<RastreoEnTiempoRealResponse> =
            trackingApiService.getUbicacionActual(codigoDeSeguimiento)

        return response.processResponse(retrofit)
    }
}