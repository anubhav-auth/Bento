package com.anubhav_auth.bento

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anubhav_auth.bento.api.BentoApiRepository
import com.anubhav_auth.bento.api.Response
import com.anubhav_auth.bento.database.entities.placesData.PlacesData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.log

class BentoViewModel(
    private val bentoApiRepository: BentoApiRepository
): ViewModel() {

    private val _placesData = MutableStateFlow<PlacesData?>(null)
    val placesData = _placesData.asStateFlow()

    private var isLoading = false //added this so the query doest run if a data is being loaded

    private val _showErrorChannel = Channel<Boolean>()
    val showErrorChannel = _showErrorChannel.receiveAsFlow()

    fun loadPlacesData(
        searchString: String
    ){
        if (!isLoading) {
            isLoading = true
            viewModelScope.launch {
               bentoApiRepository.getPlacesFromText(query = searchString).collectLatest { result->
                   when(result){
                       is Response.Error -> {
                           _showErrorChannel.send(true)
                           isLoading = false
                       }
                       is Response.Success -> {
                           result.data?.let {data->
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
}