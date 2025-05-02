package com.betondecken.trackingsystem.datasources.services.infrastructure

import com.betondecken.trackingsystem.datasources.services.UserApiService
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://10.0.2.2:7039/"

    @Provides
    @Singleton
    fun provideGson(): com.google.gson.Gson {
        // Gson no soporta OffsetDateTime, por lo que debemos deserializarlo manualmente
        val dateTimeDeserializer = JsonDeserializer<OffsetDateTime> { json, _, _ ->
            val dateTimeString = json.asString
            return@JsonDeserializer OffsetDateTime.parse(dateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }

        return GsonBuilder()
            .registerTypeAdapter(OffsetDateTime::class.java, dateTimeDeserializer)
            .create()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        //return OkHttpClient.Builder()
        return UnsafeOkHttpClient.unsafeOkHttpClient
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: com.google.gson.Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            //.client(UnsafeOkHttpClient.unsafeOkHttpClient.build())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

//    @Provides
//    @Singleton
//    fun provideTrackingApiService(retrofit: Retrofit): TrackingApiService {
//        return retrofit.create(TrackingApiService::class.java)
//    }
}