package com.anubhav_auth.bento.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anubhav_auth.bento.database.daos.SavedAddressDAO
import com.anubhav_auth.bento.database.entities.SavedAddress


@Database(entities = [SavedAddress::class], version = 1)
abstract class BentoDatabase: RoomDatabase() {
    abstract fun savedAddressDao(): SavedAddressDAO
}