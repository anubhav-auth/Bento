package com.anubhav_auth.bento.database

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anubhav_auth.bento.database.daos.SavedAddressDAO
import com.anubhav_auth.bento.database.entities.SavedAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocalDatabaseViewModel(
    private val savedAddressDAO: SavedAddressDAO
) : ViewModel() {

    private val _savedAddresses = MutableStateFlow<List<SavedAddress>>(emptyList())
    val savedAddress = _savedAddresses.asStateFlow()

    fun getSavedAddresses(){
        viewModelScope.launch {
            savedAddressDAO.getAllAddresses().collectLatest { data->
                _savedAddresses.update {
                    data
                }
            }
        }
    }

    fun saveAddress(address: SavedAddress) {
        viewModelScope.launch {
            savedAddressDAO.upsertAddress(address)
        }
    }

}