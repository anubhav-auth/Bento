package com.anubhav_auth.bento.api

sealed class Response<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : Response<T>(data)
    class Error<T>(data: T? = null, message: String?) : Response<T>(data, message)
}