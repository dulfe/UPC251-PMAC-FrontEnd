package com.betondecken.trackingsystem.entities

import java.time.OffsetDateTime

data class UsuarioResponse (
    val usuarioId: Int,
    val email: String,
    val nombres: String,
    val apellidos: String,
    val fechaDeCreacion: OffsetDateTime,
    val estado: String,
)
