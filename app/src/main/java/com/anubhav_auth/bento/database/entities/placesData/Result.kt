package com.anubhav_auth.bento.database.entities.placesData

data class Result(
    val business_status: String,
    val formatted_address: String,
    val geometry: Geometry,
    val icon: String,
    val icon_background_color: String,
    val icon_mask_base_uri: String,
    val name: String,
    val opening_hours: OpeningHours,
    val permanently_closed: Boolean,
    val photos: List<Photo>,
    val place_id: String,
    val plus_code: PlusCode,
    val rating: Double,
    val reference: String,
    val types: List<String>,
    val user_ratings_total: Int
)