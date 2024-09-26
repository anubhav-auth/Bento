package com.anubhav_auth.bento.api

import android.util.Log
import com.anubhav_auth.bento.BuildConfig
import com.anubhav_auth.bento.entities.backendRecieved.menuEntity.MenuItem
import com.anubhav_auth.bento.entities.backendRecieved.restaurantEntity.RestaurantEntityItem
import com.anubhav_auth.bento.entities.geocodeData.GeocodeData
import com.anubhav_auth.bento.entities.placesData.PlacesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BentoApiRepoImpl(private val api: Api) : BentoApiRepository {
    val bentoBaseUrl = BuildConfig.bento_base

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

    override suspend fun getAllRestaurants(): Flow<Response<List<RestaurantEntityItem>>> {
        return flow {

            val restaurantsFromAPI = try {
                api.getAllRestaurants("${bentoBaseUrl}restaurants/all-restaurants")
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(message = "Problem Loading restaurant data"))
                return@flow
            }
            emit(Response.Success(restaurantsFromAPI))

        }
    }

    override suspend fun getRestaurant(restaurantId: String): Flow<Response<RestaurantEntityItem>> {
        return flow {
            val restaurantFromAPi = try {
                api.getRestaurantById("${bentoBaseUrl}restaurants", restaurantId)
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(message = "error getting restaurant $restaurantId"))
                return@flow
            }
            emit(Response.Success(restaurantFromAPi))
        }
    }

    override suspend fun getRestaurantMenu(
        restaurantId: String
    ): Flow<Response<List<MenuItem>>> {
        return flow {

            val menuFromAPI = try {
                api.getMenu("${bentoBaseUrl}restaurants/menu", restaurantId)
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(message = "Problem loading menu of restaurant id: $restaurantId"))
                return@flow
            }
            emit(Response.Success(menuFromAPI))
        }
    }

    override suspend fun getCartItems(ids:List<String>): Flow<Response<List<MenuItem>>> {
        return flow {
            val cartItemsFromAPi = try {
                api.getCartItems("${bentoBaseUrl}cart/cart-data",ids)
            }catch (e:Exception){
                e.printStackTrace()
                emit(Response.Error(message = "error fetching cart data"))
                return@flow
            }
            emit(Response.Success(cartItemsFromAPi))
        }
    }

}