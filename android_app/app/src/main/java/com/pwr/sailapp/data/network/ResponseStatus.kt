package com.pwr.sailapp.data.network

const val TOKEN_EXPIRED = "Token has expired"
// const val

sealed class ResponseStatus
data class Error(val msg: String?) : ResponseStatus()
data class Success(val msg: String? = null) : ResponseStatus()
