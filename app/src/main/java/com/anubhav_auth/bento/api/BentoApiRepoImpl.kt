package com.anubhav_auth.bento.api

import com.anubhav_auth.bento.database.entities.placesData.PlacesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BentoApiRepoImpl(private val api: Api):BentoApiRepository {
    override suspend fun getPlacesFromText(
        query: String,
        apiKey: String
    ): Flow<Response<PlacesData>> {
        return flow {
            val placesFromApi = try {
                api.findPlace(query, apiKey)
            }catch (e:Exception){
                e.printStackTrace()
                emit(Response.Error(message = "Problem loading places data"))
                return@flow
            }
            emit(Response.Success(placesFromApi))
        }
    }

}