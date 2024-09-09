package com.anubhav_auth.bento.database.entities.placesData

import com.squareup.moshi.Json

data class Candidate(

    @Json(name = "formatted_address")
    val formattedAddress: String,
    val geometry: Geometry,
    val name: String
)