package com.betondecken.trackingsystem.entities

data class NuevoUsuarioInput(
    val email: String,
    val password: String,
    val confirmacionDePassword: String,
    val nombres: String,
    val apellidos: String
)
