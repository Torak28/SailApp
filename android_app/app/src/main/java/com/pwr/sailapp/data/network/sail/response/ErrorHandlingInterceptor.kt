package com.pwr.sailapp.data.network.sail.response

import com.pwr.sailapp.internal.ErrorCodeException
import okhttp3.Interceptor
import okhttp3.Response

class ErrorHandlingInterceptor : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if(response.isSuccessful) return response
        else throw ErrorCodeException()
    }

}