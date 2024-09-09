package com.anubhav_auth.bento.database.entities.geocodeData

import com.squareup.moshi.Json

data class Geometry(
    val bounds: Bounds,
    val location: Location,
    @Json(name = "location_type")
    val locationType: String,
    val viewport: Viewport
)