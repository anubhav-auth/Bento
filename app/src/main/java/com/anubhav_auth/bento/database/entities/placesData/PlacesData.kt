package com.anubhav_auth.bento.database.entities.placesData

data class PlacesData(
    val html_attributions: List<Any>,
    val results: List<Result>,
    val status: String
)