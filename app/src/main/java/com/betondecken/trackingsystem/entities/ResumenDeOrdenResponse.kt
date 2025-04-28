package com.betondecken.trackingsystem.entities

import java.time.OffsetDateTime

data class ResumenDeOrdenResponse(
    val ordenDeTrabajoId: Int,
    val fechaEstimadaDeEnvio: OffsetDateTime,
    val fechaEstimadaDeEntrega: OffsetDateTime,
    val fechaDeEntrega: OffsetDateTime?,
    val fechaDeEnvio: OffsetDateTime?,
    val fechaDeCreacion: OffsetDateTime,
    val codigoDeSeguimiento: String,
    val estado: String
)
