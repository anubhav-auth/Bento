package com.anubhav_auth.bento.database.entities.geocodeData

import com.squareup.moshi.Json

data class GeocodeData(

    @Json(name = "plus_code")
    val plusCode: PlusCode,
    val results: List<Result>,
    val status: String
)