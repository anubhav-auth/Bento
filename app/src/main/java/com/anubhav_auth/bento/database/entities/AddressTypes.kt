package com.anubhav_auth.bento.database.entities

enum class AddressTypes(val addressType: String){
    HOME("Home"),
    WORK("Work"),
    FRIENDS_AND_FAMILY("Friends and Family"),
    OTHERS("Others");

    fun message():String{
        return addressType
    }
    companion object {
        fun AsStringList(): List<String> {
            return entries.map { it.addressType }
        }
    }
}

