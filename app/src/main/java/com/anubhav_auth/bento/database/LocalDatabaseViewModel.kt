package com.anubhav_auth.bento.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anubhav_auth.bento.database.daos.SavedAddressDAO
import com.anubhav_auth.bento.entities.SavedAddress
import com.anubhav_auth.bento.location.saveToLocationShared
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocalDatabaseViewModel(
    private val savedAddressDAO: SavedAddressDAO
) : ViewModel() {

    private val _savedAddresses = MutableStateFlow<List<SavedAddress>>(emptyList())
    val savedAddress = _savedAddresses.asStateFlow()

    private val _lastSavedAddressId = MutableStateFlow<Long?>(null)
    val lastSavedAddressId = _lastSavedAddressId.asStateFlow()

    init {
        getSavedAddresses()
    }

    fun getSavedAddresses() {
        viewModelScope.launch {
            savedAddressDAO.getAllAddresses().collectLatest { data ->
                _savedAddresses.update {
                    data
                }
            }
        }
    }

    fun saveAddress(address: SavedAddress, context: Context) {
        viewModelScope.launch {
            val a = savedAddressDAO.upsertAddress(address)
            saveToLocationShared(context, a)
        }
    }

}