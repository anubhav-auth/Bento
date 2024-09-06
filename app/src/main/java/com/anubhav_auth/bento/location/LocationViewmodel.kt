package com.anubhav_auth.bento.location

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LocationViewmodel : ViewModel() {
    private val tag = "mytag"

    private val _latLang = MutableStateFlow<String>("london")
    val latLang = _latLang.asStateFlow()

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
                Log.d(tag,"Exception in getting current Location")
            }

    }


}