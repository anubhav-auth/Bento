package com.anubhav_auth.bento.database.entities.geocodeData

import com.squareup.moshi.Json

data class PlusCode(
    @Json(name = "compound_code")
    val compoundCode: String,
    @Json(name = "global_code")
    val globalCode: String
)