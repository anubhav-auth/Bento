package com.anubhav_auth.bento.api

import com.anubhav_auth.bento.BuildConfig
import com.anubhav_auth.bento.database.entities.geocodeData.GeocodeData
import com.anubhav_auth.bento.database.entities.placesData.PlacesData
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface Api {

    @GET
    suspend fun findPlace(
        @Url url : String,
        @Query("query") input: String,
        @Query("key") apiKey: String
    ): PlacesData

    @GET
    suspend fun findPlaceFromLatLng(
        @Url url: String,
        @Query("latlng") latLang: String,
        @Query("key") apiKey: String
    ): GeocodeData

    companion object{
        const val BASE_URL_PLACES = "https://maps.googleapis.com/"
    }
}