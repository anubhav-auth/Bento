package com.anubhav_auth.bento.api

import com.anubhav_auth.bento.api.Api.Companion.BASE_URL_PLACES
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitInstance {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(interceptor)
        .build()

    val api: Api = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL_PLACES)
        .client(client)
        .build()
        .create(Api::class.java)

}