package com.betondecken.trackingsystem.entities

import java.time.OffsetDateTime

data class RastreoEnTiempoRealResponse(
    val codigoDeSeguimiento: String,
    val latitud: Double,
    val longitud: Double,
    val fechaDeCreacion: OffsetDateTime
)