package com.revolve44.solarpanelx.domain

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val code   : Int = 0
) {
    class Init<T>() : Resource<T>(message = "initialized")
    class Success<T>(data: T) : Resource<T>(data,null)
    class Error<T>(message: String, data: T? = null, code: Int) : Resource<T>(data, message, code)
    class Loading<T> : Resource<T>()

}