package com.betondecken.trackingsystem.entities

data class SimpleError(val codigo: String, val mensaje: String) {
    override fun toString(): String {
        return "Error: $codigo - $mensaje"
    }
}
