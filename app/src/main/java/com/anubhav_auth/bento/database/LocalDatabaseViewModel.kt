package com.anubhav_auth.bento.database

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anubhav_auth.bento.database.daos.SavedAddressDAO
import com.anubhav_auth.bento.database.entities.SavedAddress
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LocalDatabaseViewModel(
    private val savedAddressDAO: SavedAddressDAO
): ViewModel() {
    val savedAddress = savedAddressDAO.getAllAddresses().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun saveAddress(address: SavedAddress){
        viewModelScope.launch {
            savedAddressDAO.upsertAddress(address)
        }
    }

}