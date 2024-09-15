package com.anubhav_auth.bento.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng


@Entity
data class SavedAddress(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val houseFlatNo: String,
    val floorNo: String,
    val apartmentBuildingName: String,
    val howToReach: String,
    val landmark: String,
    val contactNumber: Long,
    val latLang: LatLng,
    val mapAddress: String,
    val addressType: AddressTypes
)