package com.anubhav_auth.bento.api

import com.anubhav_auth.bento.BuildConfig
import com.anubhav_auth.bento.database.entities.geocodeData.GeocodeData
import com.anubhav_auth.bento.database.entities.placesData.PlacesData
import kotlinx.coroutines.flow.Flow

interface BentoApiRepository {

    suspend fun getPlacesFromText(
        query: String,
        apiKey: String = BuildConfig.maps_api
    ): Flow<Response<PlacesData>>

    suspend fun getPlaceFromLatLng(
        latLng: String,
        apiKey: String = BuildConfig.maps_api
    ): Flow<Response<GeocodeData>>

}