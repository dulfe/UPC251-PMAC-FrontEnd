package com.betondecken.trackingsystem.repositories

import com.betondecken.trackingsystem.datasources.TrackingDataSource
import com.betondecken.trackingsystem.entities.DataSourceResult
import com.betondecken.trackingsystem.entities.DetalleDeOrdenResponse
import com.betondecken.trackingsystem.entities.RepositoryResult
import com.betondecken.trackingsystem.entities.ResumenDeOrdenResponse
import javax.inject.Inject

sealed class GetOrderByTrackingCodeResult {
    data class Success(val data: DetalleDeOrdenResponse) : GetOrderByTrackingCodeResult()
    data class Error(val message: String) : GetOrderByTrackingCodeResult()
    data object NotFound : GetOrderByTrackingCodeResult()
}

class TrackingRepository @Inject constructor(
    private val trackingDataSource: TrackingDataSource
) {
    suspend fun getRecentOrders(count: Int): RepositoryResult<List<ResumenDeOrdenResponse>> {
        return when (val result = trackingDataSource.getOrdenes(count)) {
            is DataSourceResult.Success -> RepositoryResult.Success(result.data)
            is DataSourceResult.Error -> RepositoryResult.Error(result.error.mensaje)
        }
    }

    suspend fun getOrderByTrackingCode(trackingCode: String): GetOrderByTrackingCodeResult {
        return when (val result = trackingDataSource.getOrdenPorCodigoDeSeguimiento(trackingCode)) {
            is DataSourceResult.Success -> GetOrderByTrackingCodeResult.Success(result.data)
            is DataSourceResult.Error -> {
                if (result.error.codigo == "404") {
                    return GetOrderByTrackingCodeResult.NotFound
                }

                return GetOrderByTrackingCodeResult.Error(result.error.mensaje)
            }
        }
    }

    suspend fun deleteOrdenPorUsuario(codigoDeSeguimiento: String): RepositoryResult<Unit> {
        return when (val result = trackingDataSource.deleteOrdenPorUsuario(codigoDeSeguimiento)) {
            is DataSourceResult.Success -> RepositoryResult.Success(result.data)
            is DataSourceResult.Error -> RepositoryResult.Error(result.error.mensaje)
        }
    }
}