package com.anubhav_auth.bento.database.entities.placesData

data class Photo(
    val height: Int,
    val html_attributions: List<String>,
    val photo_reference: String,
    val width: Int
)