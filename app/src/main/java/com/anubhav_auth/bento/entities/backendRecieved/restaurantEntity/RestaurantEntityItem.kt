package com.anubhav_auth.bento.entities.backendRecieved.restaurantEntity

data class RestaurantEntityItem(
    val address: Address,
    val createdAt: String,
    val cuisines: List<String>,
    val currentOrders: List<Any>,
    val email: String,
    val imageUrl: String,
    val id: String,
    val menuItems: List<String>,
    val name: String,
    val openingHours: List<OpeningHour>,
    val ownerId: String,
    val pastOrders: List<Any>,
    val paymentIds: List<Any>,
    val phone: String,
    val rating: Any,
    val reviews: Any,
    val updatedAt: String
)