package com.betondecken.trackingsystem.datasources

import com.betondecken.trackingsystem.entities.DataSourceResult
import com.betondecken.trackingsystem.entities.DetalleDeOrdenResponse
import com.betondecken.trackingsystem.entities.RastreoEnTiempoRealResponse
import com.betondecken.trackingsystem.entities.ResumenDeOrdenResponse
import javax.inject.Inject
import kotlinx.coroutines.delay
import java.time.OffsetDateTime

class TrackingDataSource @Inject constructor() {
    suspend fun getOrdenes(cantidad: Int): DataSourceResult<List<ResumenDeOrdenResponse>> {
        delay(2000)

        // Retornar un ejemplo
        val trackingData = listOf(
            ResumenDeOrdenResponse(
                estado = "C",
                fechaDeCreacion = OffsetDateTime.now(),
                ordenDeTrabajoId = 1,
                fechaDeEnvio = OffsetDateTime.now(),
                codigoDeSeguimiento = "TRK123456789",
                fechaEstimadaDeEnvio = OffsetDateTime.now(),
                fechaDeEntrega = OffsetDateTime.now(),
                fechaEstimadaDeEntrega = OffsetDateTime.now()
            ),
            ResumenDeOrdenResponse(
                estado = "P",
                fechaDeCreacion = OffsetDateTime.now(),
                ordenDeTrabajoId = 2,
                fechaDeEnvio = OffsetDateTime.now(),
                codigoDeSeguimiento = "TRK987654321",
                fechaEstimadaDeEnvio = OffsetDateTime.now(),
                fechaDeEntrega = OffsetDateTime.now(),
                fechaEstimadaDeEntrega = OffsetDateTime.now()
            ),
            ResumenDeOrdenResponse(
                estado = "P",
                fechaDeCreacion = OffsetDateTime.now(),
                ordenDeTrabajoId = 2,
                fechaDeEnvio = OffsetDateTime.now(),
                codigoDeSeguimiento = "TRK112233445",
                fechaEstimadaDeEnvio = OffsetDateTime.now(),
                fechaDeEntrega = OffsetDateTime.now(),
                fechaEstimadaDeEntrega = OffsetDateTime.now()
            ),
            ResumenDeOrdenResponse(
                estado = "X",
                fechaDeCreacion = OffsetDateTime.now(),
                ordenDeTrabajoId = 2,
                fechaDeEnvio = OffsetDateTime.now(),
                codigoDeSeguimiento = "TRK556677889",
                fechaEstimadaDeEnvio = OffsetDateTime.now(),
                fechaDeEntrega = OffsetDateTime.now(),
                fechaEstimadaDeEntrega = OffsetDateTime.now()
            ),
            ResumenDeOrdenResponse(
                estado = "C",
                fechaDeCreacion = OffsetDateTime.now(),
                ordenDeTrabajoId = 2,
                fechaDeEnvio = OffsetDateTime.now(),
                codigoDeSeguimiento = "TRK998877665",
                fechaEstimadaDeEnvio = OffsetDateTime.now(),
                fechaDeEntrega = OffsetDateTime.now(),
                fechaEstimadaDeEntrega = OffsetDateTime.now()
            ),
            ResumenDeOrdenResponse(
                estado = "C",
                fechaDeCreacion = OffsetDateTime.now(),
                ordenDeTrabajoId = 2,
                fechaDeEnvio = OffsetDateTime.now(),
                codigoDeSeguimiento = "TRK900877667",
                fechaEstimadaDeEnvio = OffsetDateTime.now(),
                fechaDeEntrega = OffsetDateTime.now(),
                fechaEstimadaDeEntrega = OffsetDateTime.now()
            )
        )

        return DataSourceResult.Success(trackingData)
    }

    suspend fun getOrdenPorCodigoDeSeguimiento(codigoDeSeguimiento: String): DataSourceResult<DetalleDeOrdenResponse> {
        delay(2000)

        // Retornar un ejemplo
        val trackingData = DetalleDeOrdenResponse(
            estado = "P",
            fechaDeCreacion = OffsetDateTime.now(),
            ordenDeTrabajoId = 1,
            fechaDeEnvio = OffsetDateTime.now(),
            codigoDeSeguimiento = "123456",
            fechaEstimadaDeEnvio = OffsetDateTime.now(),
            fechaDeEntrega = OffsetDateTime.now(),
            fechaEstimadaDeEntrega = OffsetDateTime.now(),
            clienteId = 1,
            fabricaId = 1,
            conductorNombres = "Juan",
            conductorId = 1,
            pesoEnKilos = 1.3,
            conductorTelefono = "123456789",
            nombreDeLaFabrica = "Fabrica 1",
            conductorApellidos = "Perez",
            direccionDeEntrega = "Av. Argentina 4793, Callao 07041, Peru",
            lugarDeEntrega = "Alicorp S.A.A.",
            envioId = 1,
            fechaEstimadaDeTermino = OffsetDateTime.now(),

            direccionDeEntregaLatitud = -12.048460385792891,
            direccionDeEntregaLongitud = -77.09662151852136,
        )

        return DataSourceResult.Success(trackingData)
    }

    suspend fun deleteOrdenPorUsuario(codigoDeSeguimiento: String): DataSourceResult<Int> {
        delay(2000)

        // Retornar un ejemplo
        val trackingData = 1

        return DataSourceResult.Success(trackingData)
    }

    suspend fun getUbicacionActual(codigoDeSeguimiento: String): DataSourceResult<RastreoEnTiempoRealResponse> {
        delay(2000)

        // Retornar un ejemplo
        val trackingData = RastreoEnTiempoRealResponse(
            latitud = 1.0,
            longitud = 1.0,
            fechaDeCreacion =  OffsetDateTime.now(),
            codigoDeSeguimiento = codigoDeSeguimiento
        )

        return DataSourceResult.Success(trackingData)
    }
}