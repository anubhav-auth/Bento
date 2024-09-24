package com.anubhav_auth.bento.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anubhav_auth.bento.database.daos.SavedAddressDAO
import com.anubhav_auth.bento.entities.SavedAddress


@Database(entities = [SavedAddress::class], version = 1)
@TypeConverters(LatLngTypeConvertor::class)
abstract class BentoDatabase : RoomDatabase() {
    abstract fun savedAddressDao(): SavedAddressDAO
}