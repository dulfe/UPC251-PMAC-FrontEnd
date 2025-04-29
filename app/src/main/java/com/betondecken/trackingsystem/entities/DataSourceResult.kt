package com.betondecken.trackingsystem.entities

sealed class DataSourceResult<out T> {
    data class Success<T>(val data: T) : DataSourceResult<T>()
    data class Error(val error: SimpleError) : DataSourceResult<Nothing>()
}