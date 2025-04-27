package com.betondecken.trackingsystem.entities

import java.time.OffsetDateTime

data class Usuario(
    val usuarioId: Int,
    val email: String,
    val passwordHash: String,
    val nombres: String,
    val apellidos: String,
    val fechaDeCreacion: OffsetDateTime,
    val estado: String,
)
