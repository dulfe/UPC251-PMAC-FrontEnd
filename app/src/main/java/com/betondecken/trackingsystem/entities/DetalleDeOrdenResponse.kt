package com.betondecken.trackingsystem.entities

import java.time.OffsetDateTime

data class DetalleDeOrdenResponse(
    val ordenDeTrabajoId: Int,
    val fechaEstimadaDeTermino: OffsetDateTime,
    val fechaEstimadaDeEnvio: OffsetDateTime,
    val fechaEstimadaDeEntrega: OffsetDateTime,
    val fechaDeEntrega: OffsetDateTime?,
    val fechaDeEnvio: OffsetDateTime?,
    val fechaDeCreacion: OffsetDateTime,
    val clienteId: Int,
    val codigoDeSeguimiento: String,
    val lugarDeEntrega: String,
    val direccionDeEntrega: String,
    val pesoEnKilos: Double,
    val fabricaId: Int,
    val nombreDeLaFabrica: String,
    val envioId: Int?,
    val estado: String,
    val conductorId: Int?,
    val conductorNombres: String?,
    val conductorApellidos: String?,
    val conductorTelefono: String?,

    val direccionDeEntregaLatitud: Double,
    val direccionDeEntregaLongitud: Double,
)
