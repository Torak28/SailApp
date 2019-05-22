package com.pwr.sailapp.data.network.weather

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.pwr.sailapp.data.network.sail.ConnectivityInterceptor
import com.pwr.sailapp.data.weather.Weather
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/*
https://api.darksky.net/forecast/55da7eaf53d41d33ac5f3ecf732b6796/54.692867,18.691693,2019-05-23T15:23:00 // YYYY-MM-DDTHH:MM:SS
or
https://api.darksky.net/forecast/55da7eaf53d41d33ac5f3ecf732b6796/54.692867,18.691693,1558617780 // timestamp
 */

const val DARK_SKY_URL = "https://api.darksky.net/forecast/55da7eaf53d41d33ac5f3ecf732b6796/"
// const val DARK_SKY_API_KEY = "55da7eaf53d41d33ac5f3ecf732b6796"
const val DEFAULT_UNITS = "ca"

interface DarkSkyApiService {

    @GET("{latitude},{longitude},{timestamp}")
    fun getForecast(
        @Path(value="latitude") latitude:String,
        @Path(value="longitude") longitude:String,
        @Path(value = "timestamp") timestamp: String,
        @Query("units") units: String = DEFAULT_UNITS
    ): Deferred<Weather>

    companion object{
        // syntax: ApixuWeatherApiService()
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): DarkSkyApiService {

            val requestInterceptor = Interceptor{
                val url = it.request()
                    .url()
                    .newBuilder()
                    .build()

                val request = it.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor it.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(DARK_SKY_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory()) // to use coroutines instead of of calls
                .addConverterFactory(GsonConverterFactory.create()) // use Gson to parse Json
                .build()
                .create(DarkSkyApiService::class.java)
        }
    }
}