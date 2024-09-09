package com.anubhav_auth.bento.database.entities.geocodeData

import com.squareup.moshi.Json

data class Result(
    @Json(name = "address_components")
    val addressComponents: List<AddressComponent>,
    @Json(name = "formatted_address")
    val formattedAddress: String,
    val geometry: Geometry,
    @Json(name = "place_id")
    val placeId: String,
    @Json(name = "plus_code")
    val plusCode: PlusCode,
    val types: List<String>
)