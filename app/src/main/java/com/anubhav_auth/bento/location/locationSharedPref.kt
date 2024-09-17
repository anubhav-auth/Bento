package com.anubhav_auth.bento.location

import android.content.Context
import android.util.Log

fun saveToLocationShared(context: Context, value: Long) {
    val sharedPreferences =
        context.getSharedPreferences("com.anubhav_auth.bento", Context.MODE_PRIVATE)
    Log.d("mytag", "$value")
    sharedPreferences
        .edit()
        .putLong("selected_address", value)
        .apply()
}

fun getFromLocationShared(context: Context, defaultValue: Long = -1): Long {
    val sharedPreferences =
        context.getSharedPreferences("com.anubhav_auth.bento", Context.MODE_PRIVATE)

    return sharedPreferences.getLong("selected_address", defaultValue)
}