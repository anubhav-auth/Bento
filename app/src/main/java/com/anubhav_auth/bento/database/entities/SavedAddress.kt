package com.anubhav_auth.bento.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng


@Entity
data class SavedAddress(
    @PrimaryKey(autoGenerate = true)
    private val id: Long = 0,
    private val houseFlatNo: String,
    private val floorNo: String,
    private val apartmentBuildingName: String,
    private val howToReach: String,
    private val landmark: String,
    private val contactNumber: Long,
    private val latLang: LatLng,
    private val mapAddress: String,
    private val addressType: AddressType
)

enum class AddressType{
    ENUM,
    OFFICE,
    OTHERS
}
