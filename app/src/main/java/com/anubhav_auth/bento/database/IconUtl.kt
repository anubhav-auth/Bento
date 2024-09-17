package com.anubhav_auth.bento.database

import com.anubhav_auth.bento.R
import com.anubhav_auth.bento.database.entities.AddressTypes

object IconUtl {

    fun getIconId(addressTypes: AddressTypes): Int {
        return when (addressTypes) {
            AddressTypes.HOME -> R.drawable.home
            AddressTypes.WORK -> R.drawable.business
            AddressTypes.FRIENDS_AND_FAMILY -> R.drawable.friends
            AddressTypes.OTHERS -> R.drawable.location
        }
    }

}