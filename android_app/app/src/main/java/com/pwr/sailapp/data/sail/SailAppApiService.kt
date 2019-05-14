package com.pwr.sailapp.data.sail

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.pwr.sailapp.data.sail.response.AllCentreGearResponse
import com.pwr.sailapp.data.sail.response.CentresResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/*
getCentres: https://0e4682b3-c081-4689-b8c5-51f3f0a7ae09.mock.pstmn.io/getCentres
getAllCentreGear: https://0e4682b3-c081-4689-b8c5-51f3f0a7ae09.mock.pstmn.io/getAllCentreGear?centre_id=1
 */

const val SERVER_URL = "https://0e4682b3-c081-4689-b8c5-51f3f0a7ae09.mock.pstmn.io"

interface SailAppApiService {

    @GET("getCentres")
    fun getCentres(): Deferred<CentresResponse> // defer - odraczać

    @GET("getAllCentreGear")
    fun getAllCentreGear(
        @Query("centre_id") centreID: Int
    ): Deferred<AllCentreGearResponse>

    companion object{
        // syntax: SailAppApiService()
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): SailAppApiService{
            /* okhttp3 Interceptor
            Interceptors are a powerful mechanism that can monitor, rewrite, and retry calls
            Typically interceptors add, remove, or transform headers on the request or response.
             */
            val requestInterceptor = Interceptor{
                val request = it.request()
                    .newBuilder()
                    .build()

                return@Interceptor it.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(SERVER_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create()) // use Gson to parse Json
                .build()
                .create(SailAppApiService::class.java)
        }
    }
}