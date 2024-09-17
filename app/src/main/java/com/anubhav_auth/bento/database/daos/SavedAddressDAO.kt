package com.anubhav_auth.bento.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.anubhav_auth.bento.database.entities.SavedAddress
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedAddressDAO {

    @Upsert
    suspend fun upsertAddress(savedAddress: SavedAddress):Long

    @Delete
    suspend fun deleteAddress(savedAddress: SavedAddress)

    @Query("SELECT * FROM SAVEDADDRESS")
    fun getAllAddresses(): Flow<List<SavedAddress>>
}