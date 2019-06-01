package com.pwr.sailapp.internal

import java.io.IOException

const val TOKEN_EXPIRED : String = "Token has expired"

class NoConnectivityException : IOException()

class ErrorCodeException(val code: Int, val statusMsg: String) : IOException()