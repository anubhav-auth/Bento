package com.anubhav_auth.bento.database.entities.geocodeData

import com.squareup.moshi.Json

data class AddressComponent(
    @Json(name = "long_name")
    val longName: String,
    val shortName: String,
    val types: List<String>
)