package com.anubhav_auth.bento.api

import com.anubhav_auth.bento.database.entities.geocodeData.GeocodeData
import com.anubhav_auth.bento.database.entities.placesData.PlacesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BentoApiRepoImpl(private val api: Api) : BentoApiRepository {

    override suspend fun getPlacesFromText(
        query: String,
        apiKey: String
    ): Flow<Response<PlacesData>> {
        return flow {
            val url = "https://maps.googleapis.com/maps/api/place/textsearch/json"
            val placesFromApi = try {
                api.findPlace(url, query, apiKey)
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(message = "Problem loading places data"))
                return@flow
            }
            emit(Response.Success(placesFromApi))
        }
    }


    override suspend fun getPlaceFromLatLng(
        latLng: String,
        apiKey: String
    ): Flow<Response<GeocodeData>> {
        return flow {
            val url = "https://maps.googleapis.com/maps/api/geocode/json"

            val placeFromLatLng = try {
                api.findPlaceFromLatLng(url, latLng, apiKey)
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(message = "Problem loading place data from latlang"))
                return@flow
            }
            emit(Response.Success(placeFromLatLng))
        }
    }

}