package com.anubhav_auth.bento.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anubhav_auth.bento.database.entities.placesData.Geometry
import com.google.android.gms.maps.model.LatLng


@Entity
data class SavedAddress(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val houseFlatNo: String,
    val floorNo: String,
    val apartmentBuildingName: String,
    val howToReach: String,
    val contactNumber: String,
    val latLng: LatLng,
    val mapAddress: String,
    val addressType: AddressTypes
)