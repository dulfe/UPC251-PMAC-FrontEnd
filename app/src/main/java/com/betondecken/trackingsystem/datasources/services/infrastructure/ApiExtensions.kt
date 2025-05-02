package com.betondecken.trackingsystem.datasources.services.infrastructure

import com.betondecken.trackingsystem.entities.DataSourceResult
import com.betondecken.trackingsystem.entities.SimpleError
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit

suspend fun <T> Response<T>.processResponse(retrofit: Retrofit): DataSourceResult<T> {
    if (isSuccessful) {
        return body()?.let { DataSourceResult.Success(it) }
            ?: DataSourceResult.Error(SimpleError("EMPTY_BODY", "Respuesta exitosa pero cuerpo vacío"))
    }

    // Manejo de respuesta de error
    val code = code()
    val errorBodyContent = errorBody()

    // Si el cuerpo del error está vacío, devolver un error generico
    if (errorBodyContent == null || errorBodyContent.contentLength() == 0L) {
        return DataSourceResult.Error(SimpleError(code.toString(), "Error HTTP $code: Cuerpo del error vacío."))
    }

    // Intentar procesar el cuerpo del error
    return try {
        val errorConverter: Converter<ResponseBody, SimpleError> = retrofit.responseBodyConverter(
            SimpleError::class.java,
            emptyArray()
        )
        val error = errorConverter.convert(errorBodyContent)
        DataSourceResult.Error(error ?: SimpleError(code.toString(), "Error desconocido"))
    } catch (e: Exception) {
        DataSourceResult.Error(SimpleError(code.toString(), "Error HTTP $code: Error al procesar el cuerpo del error."))
    } finally {
        errorBodyContent.close()
    }
}