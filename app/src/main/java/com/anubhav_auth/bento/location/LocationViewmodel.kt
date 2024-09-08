package com.anubhav_auth.bento.location

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationViewmodel : ViewModel() {
    private val tag = "mytag"

    private val _latLang = MutableStateFlow<String>("london")
    val latLang = _latLang.asStateFlow()

    private val _showErrorChannel = Channel<Boolean>()
    val showErrorChannel = _showErrorChannel.receiveAsFlow()

    @SuppressLint("MissingPermission")
    fun getLatLang(
        fusedLocationProviderClient: FusedLocationProviderClient,
        priority: Boolean = true
    ) {
        val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
        else Priority.PRIORITY_BALANCED_POWER_ACCURACY

        fusedLocationProviderClient.getCurrentLocation(accuracy, CancellationTokenSource().token)
            .addOnSuccessListener { location->
                _latLang.update {
                    "${location.latitude},${location.longitude}"
                }
            }
            .addOnFailureListener{ e->
                viewModelScope.launch {
                    _showErrorChannel.send(true)
                }
                Log.d(tag,"Exception in getting current Location")
            }
    }

    @SuppressLint("MissingPermission")
    fun getLatLangLast(
        fusedLocationProviderClient: FusedLocationProviderClient,
        priority: Boolean = true
    ) {
        val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
        else Priority.PRIORITY_BALANCED_POWER_ACCURACY

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location->
                _latLang.update {
                    "${location.latitude},${location.longitude}"
                }
            }
            .addOnFailureListener{ e->
                viewModelScope.launch {
                    _showErrorChannel.send(true)
                }
                Log.d(tag,"Exception in getting current Location")
            }

    }


}