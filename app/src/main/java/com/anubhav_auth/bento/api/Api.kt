package com.anubhav_auth.bento.api


import com.anubhav_auth.bento.entities.backendRecieved.menuEntity.MenuItemItem
import com.anubhav_auth.bento.entities.backendRecieved.restaurantEntity.RestaurantEntityItem
import com.anubhav_auth.bento.entities.backendRecieved.restaurantEntity.Restaurants
import com.anubhav_auth.bento.entities.geocodeData.GeocodeData
import com.anubhav_auth.bento.entities.placesData.PlacesData
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface Api {

    @GET
    suspend fun findPlace(
        @Url url: String,
        @Query("query") input: String,
        @Query("key") apiKey: String
    ): PlacesData

    @GET
    suspend fun findPlaceFromLatLng(
        @Url url: String,
        @Query("latlng") latLang: String,
        @Query("key") apiKey: String
    ): GeocodeData

    @GET
    suspend fun getAllRestaurants(
        @Url usr:String
    ):List<RestaurantEntityItem>

    @GET
    suspend fun getMenu(
        @Url url:String,
        @Query("restaurantId") restaurantID: String
    ):List<MenuItemItem>

    companion object {
        const val BASE_URL_PLACES = "https://maps.googleapis.com/"
    }
}