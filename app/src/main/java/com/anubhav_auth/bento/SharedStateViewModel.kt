package com.anubhav_auth.bento

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SharedStateViewModel : ViewModel() {

    var allPermissionGranted = mutableStateOf(false)
    var selectedAddressIndex = mutableStateOf(0)


}