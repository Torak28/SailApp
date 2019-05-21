package com.pwr.sailapp.data.network.sail

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.pwr.sailapp.data.weather.Forecast
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/*
http://api.apixu.com/v1/forecast.json?key=5ba44124a1754dd8919141307192403&q=48.8567,2.3508&days=10
 */

const val APIXU_URL = "http://api.apixu.com/v1/forecast.json"

const val APIXU_API_KEY = "5ba44124a1754dd8919141307192403"

interface ApixuWeatherApiService {

    @GET("getForecast")
    fun getForecast(
        @Query("q") location: String,
        @Query("days") days: String
    ): Deferred<Forecast> // defer - odraczać

    companion object{
        // syntax: ApixuWeatherApiService()
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): ApixuWeatherApiService{

            val requestInterceptor = Interceptor{
                val url = it.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("key", APIXU_API_KEY)
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
                .baseUrl(APIXU_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory()) // to use coroutines instead of of calls
                .addConverterFactory(GsonConverterFactory.create()) // use Gson to parse Json
                .build()
                .create(ApixuWeatherApiService::class.java)
        }
    }
}