package com.betondecken.trackingsystem.datasources.services

import com.betondecken.trackingsystem.entities.DetalleDeOrdenResponse
import com.betondecken.trackingsystem.entities.RastreoEnTiempoRealResponse
import com.betondecken.trackingsystem.entities.ResumenDeOrdenResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrackingApiService {
    @GET("/api/SeguimientoDeOrdenes")
    suspend fun getOrdenes(@Query("cantidad") cantidad: Int)
        : Response<List<ResumenDeOrdenResponse>>

    @GET("/api/SeguimientoDeOrdenes/{codigoDeSeguimiento}")
    suspend fun getOrdenPorCodigoDeSeguimiento(
        @Path("codigoDeSeguimiento") codigoDeSeguimiento: String)
        : Response<DetalleDeOrdenResponse>

    @GET("/api/SeguimientoDeOrdenes/{codigoDeSeguimiento}/ubicacion")
    suspend fun getUbicacionActual(
        @Path("codigoDeSeguimiento") codigoDeSeguimiento: String)
            : Response<RastreoEnTiempoRealResponse>

    @DELETE("/api/SeguimientoDeOrdenes/{codigoDeSeguimiento}")
    suspend fun deleteOrdenPorUsuario(
        @Path("codigoDeSeguimiento") codigoDeSeguimiento: String)
            : Response<Unit>
}