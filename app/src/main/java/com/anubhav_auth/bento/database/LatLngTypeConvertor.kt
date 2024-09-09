package com.anubhav_auth.bento.database

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng

class LatLngTypeConvertor {

    @TypeConverter
    fun fromLatLng(latLng: LatLng?):String?{
        return latLng?.let { "${it.latitude},${it.longitude}" }
    }

    @TypeConverter
    fun toLatLng(value: String?):LatLng?{
        return value?.let {
            val parts = it.split(",")
            LatLng(parts[0].toDouble(), parts[2].toDouble())
        }
    }
}