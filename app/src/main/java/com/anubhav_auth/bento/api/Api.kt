package com.anubhav_auth.bento.api

import com.anubhav_auth.bento.BuildConfig
import com.anubhav_auth.bento.database.entities.placesData.PlacesData
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("place/textsearch/json")
    suspend fun findPlace(
        @Query("query") input: String,
        @Query("key") apiKey: String = BuildConfig.maps_api
    ): PlacesData

    companion object{
        const val BASE_URL_PLACES = "https://maps.googleapis.com/maps/api/"
    }
}