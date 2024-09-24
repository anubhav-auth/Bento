package com.anubhav_auth.bento.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anubhav_auth.bento.api.BentoApiRepository
import com.anubhav_auth.bento.api.Response
import com.anubhav_auth.bento.entities.backendRecieved.menuEntity.MenuItemItem
import com.anubhav_auth.bento.entities.backendRecieved.restaurantEntity.RestaurantEntityItem
import com.anubhav_auth.bento.entities.backendRecieved.restaurantEntity.Restaurants
import com.anubhav_auth.bento.entities.geocodeData.GeocodeData
import com.anubhav_auth.bento.entities.placesData.PlacesData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BentoViewModel(
    private val bentoApiRepository: BentoApiRepository
) : ViewModel() {

    private val _placesData = MutableStateFlow<PlacesData?>(null)
    val placesData = _placesData.asStateFlow()

    private val _geocodeData = MutableStateFlow<GeocodeData?>(null)
    val geocodeData = _geocodeData.asStateFlow()

    private val _allRestaurantData = MutableStateFlow<List<RestaurantEntityItem>>(emptyList())
    val allRestaurantData = _allRestaurantData.asStateFlow()

    private val _menuData = MutableStateFlow<List<MenuItemItem>>(emptyList())
    val menuData = _menuData.asStateFlow()

    private var isLoading = false //added this so the query doest run if a data is being loaded

    private val _showErrorChannel = Channel<Boolean>()
    val showErrorChannel = _showErrorChannel.receiveAsFlow()

    init {
        loadAllRestaurantData()
        Log.d("mytag1", allRestaurantData.toString())
    }

    fun loadPlacesData(
        searchString: String
    ) {
        if (!isLoading) {
            isLoading = true
            viewModelScope.launch {
                bentoApiRepository.getPlacesFromText(query = searchString).collectLatest { result ->
                    when (result) {
                        is Response.Error -> {
                            _showErrorChannel.send(true)
                            isLoading = false
                        }

                        is Response.Success -> {
                            result.data?.let { data ->
                                _placesData.update {
                                    data
                                }
                            }
                            isLoading = false
                        }
                    }
                }
            }
        }
    }

    fun clearLoadedPlacesDate() {
        _placesData.update {
            null
        }
    }

    fun loadPLaceDataFromLatLang(
        latLng: LatLng
    ) {
        if (!isLoading) {
            isLoading = true
            viewModelScope.launch {
                bentoApiRepository.getPlaceFromLatLng("${latLng.latitude},${latLng.longitude}")
                    .collectLatest { result ->
                        when (result) {
                            is Response.Error -> {
                                _showErrorChannel.send(true)
                                isLoading = false
                            }

                            is Response.Success -> {
                                _geocodeData.update {
                                    result.data
                                }
                                isLoading = false
                            }
                        }
                    }
            }
        }
    }

    fun loadAllRestaurantData(){
        if (!isLoading) {
            isLoading = true
            viewModelScope.launch {
                bentoApiRepository.getAllRestaurants().collectLatest { value: Response<List<RestaurantEntityItem>> ->
                    when(value){
                        is Response.Error -> {
                            _showErrorChannel.send(true)
                            isLoading = false
                        }

                        is Response.Success -> {
                            value.data?.let { restaurants->
                                _allRestaurantData.update {
                                    restaurants
                                }
                            }
                            isLoading = false
                        }
                    }
                }
            }
        }
    }

    fun loadMenuData(restaurantId: String){
        if (!isLoading) {
            isLoading = true
            viewModelScope.launch {
                bentoApiRepository.getRestaurantMenu(restaurantId).collectLatest { value: Response<List<MenuItemItem>> ->
                    when(value){
                        is Response.Error -> {
                            _showErrorChannel.send(true)
                            isLoading = false
                        }

                        is Response.Success -> {
                            value.data?.let { menu->
                                _menuData.update {
                                    menu
                                }
                            }
                            isLoading = false
                        }
                    }
                }
            }
        }
    }
}