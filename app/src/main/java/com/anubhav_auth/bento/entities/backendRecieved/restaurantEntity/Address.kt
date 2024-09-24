package com.anubhav_auth.bento.entities.backendRecieved.restaurantEntity

data class Address(
    val addressType: String,
    val apartmentBuildingNo: String,
    val contactNumber: String,
    val floorNo: String,
    val houseFlatNo: String,
    val howToReach: String,
    val latLang: Any,
    val mapAddress: String,
    val name: String
)