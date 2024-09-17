package com.anubhav_auth.bento

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.anubhav_auth.bento.database.entities.placesData.Result

class SharedStateViewModel : ViewModel() {

    var allPermissionGranted = mutableStateOf(false)
    var selectedAddressIndex = mutableStateOf(0)
    var searchedAddress = mutableStateOf<Result?>(null)
    var currentlyActiveSavedAddress = mutableStateOf(-1)
}