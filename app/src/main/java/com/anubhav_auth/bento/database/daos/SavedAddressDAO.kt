package com.anubhav_auth.bento.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert
import com.anubhav_auth.bento.database.entities.SavedAddress

@Dao
interface SavedAddressDAO {
    @Upsert
    suspend fun upsertAddress(savedAddress: SavedAddress)

    @Delete
    suspend fun deleteAddress(savedAddress: SavedAddress)
}