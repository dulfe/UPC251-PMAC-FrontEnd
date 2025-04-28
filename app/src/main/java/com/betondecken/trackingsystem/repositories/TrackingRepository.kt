package com.betondecken.trackingsystem.repositories

import com.betondecken.trackingsystem.datasources.TrackingDataSource
import com.betondecken.trackingsystem.entities.DataSourceResult
import com.betondecken.trackingsystem.entities.ResumenDeOrdenResponse
import javax.inject.Inject

sealed class GetOrdersResult {
    data class Success(val orders: List<ResumenDeOrdenResponse>) : GetOrdersResult()
    data class Error(val message: String) : GetOrdersResult()
}

class TrackingRepository @Inject constructor(
    private val trackingDataSource: TrackingDataSource
) {
    suspend fun getRecentOrders(count: Int): GetOrdersResult {
        return when (val result = trackingDataSource.getOrdenes(count)) {
            is DataSourceResult.Success -> GetOrdersResult.Success(result.data)
            is DataSourceResult.Error -> GetOrdersResult.Error(result.error.mensaje)
        }
    }
}