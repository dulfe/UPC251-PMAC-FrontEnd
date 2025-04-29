package com.betondecken.trackingsystem

// Utilidad para manejar eventos one-time (navegación, Toast, etc.)
// Esto previene que el mismo evento se dispare múltiples veces en cambios de configuración
open class Event<out T>(private val content: T) {

    private var hasBeenHandled = false
        private set // Solo permite setear internamente

    /**
     * Retorna el contenido y marca el evento como manejado.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Retorna el contenido, incluso si ya ha sido manejado.
     */
    fun peekContent(): T = content
}