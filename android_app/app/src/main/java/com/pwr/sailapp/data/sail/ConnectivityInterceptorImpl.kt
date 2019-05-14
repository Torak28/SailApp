package com.pwr.sailapp.data.sail

import android.content.Context
import android.net.ConnectivityManager
import com.pwr.sailapp.internal.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptorImpl(
    context: Context // to get system service to check connection
) : ConnectivityInterceptor {

    private val appContext = context.applicationContext // safe way - no depend on lifecycle

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isOnline()) throw NoConnectivityException()
        return chain.proceed(chain.request())
    }

    private fun isOnline(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}