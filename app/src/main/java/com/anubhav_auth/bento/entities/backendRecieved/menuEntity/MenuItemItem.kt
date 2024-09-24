package com.anubhav_auth.bento.entities.backendRecieved.menuEntity

data class MenuItemItem(
    val availability: Boolean,
    val category: String,
    val createdAt: String,
    val description: String,
    val id: String,
    val imageUrl: String,
    val name: String,
    val price: Int,
    val restaurantId: String,
    val updatedAt: String
)