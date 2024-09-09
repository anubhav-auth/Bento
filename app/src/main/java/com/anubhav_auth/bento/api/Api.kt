package com.anubhav_auth.bento.api

interface Api {
    companion object{
        const val BASE_URL_PLACES = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?fields=formatted_address%2Cname%2Cgeometry&input=kal&inputtype=textquery&key=AIzaSyCqWchk4GwuLg4_WUipmkJ0s62UxojO1iw"
    }
}