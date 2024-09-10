package com.anubhav_auth.bento.api

import com.anubhav_auth.bento.BuildConfig
import com.anubhav_auth.bento.database.entities.placesData.PlacesData
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("place/findplacefromtext/json")
    suspend fun findPlace(
        @Query("fields") fields: String = "formatted_address,name,geometry",
        @Query("input") input: String,
        @Query("inputtype") inputType: String = "textquery",
        @Query("key") apiKey: String = BuildConfig.maps_api
    ): PlacesData

    companion object{
        const val BASE_URL_PLACES = "https://maps.googleapis.com/maps/api/"
    }
}