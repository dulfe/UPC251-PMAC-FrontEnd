package com.betondecken.trackingsystem.entities

/**
 * Esto es un resultado GENERICO para el repositorio.
 * Actualmente es similar a DataSourceResult, pero la idea es que
 * sirva como un resultado generico hasta que los repositorios tengan
 * una estructura mas detallada.
 *
 * @param T The type of the data returned on success.
 */
sealed class RepositoryResult<out T> {
    data class Success<T>(val data: T) : RepositoryResult<T>()
    data class Error(val error: String) : RepositoryResult<Nothing>()
}