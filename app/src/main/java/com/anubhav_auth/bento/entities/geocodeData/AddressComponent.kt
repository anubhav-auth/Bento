package com.anubhav_auth.bento.entities.geocodeData

import com.squareup.moshi.Json

data class AddressComponent(
    @Json(name = "long_name")
    val long_name: String,
    @Json(name = "short_name")
    val shortName: String,
    val types: List<String>
)