package com.anubhav_auth.bento.api

import com.anubhav_auth.bento.BuildConfig
import com.anubhav_auth.bento.entities.backendRecieved.menuEntity.MenuItem
import com.anubhav_auth.bento.entities.backendRecieved.restaurantEntity.RestaurantEntityItem
import com.anubhav_auth.bento.entities.geocodeData.GeocodeData
import com.anubhav_auth.bento.entities.placesData.PlacesData
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

    suspend fun getAllRestaurants(): Flow<Response<List<RestaurantEntityItem>>>
    suspend fun getRestaurant(restaurantId: String): Flow<Response<RestaurantEntityItem>>
    suspend fun getRestaurantMenu(restaurantId: String): Flow<Response<List<MenuItem>>>
    suspend fun getCartItems(ids:List<String>):Flow<Response<List<MenuItem>>>
}