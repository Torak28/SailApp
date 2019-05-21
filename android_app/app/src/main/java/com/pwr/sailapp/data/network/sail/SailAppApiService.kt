package com.pwr.sailapp.data.network.sail

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.pwr.sailapp.data.sail.User
import com.pwr.sailapp.data.sail.UserCredentials
import com.pwr.sailapp.data.network.sail.response.*
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

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

    @GET("getAllUserRentals")
    fun getAllUserRentals(
        @Query("user_id") userID: Int
    ):Deferred<AllUserRentalsResponse>

    @POST("registerUser")
    fun registerUserAsync(
        @Body user: User
    ):Deferred<RegisterUserResponse>

    @POST("loginUser")
    fun loginUserAsync(
        @Body userCredentials: UserCredentials
    ):Deferred<LoginUserResponse>

    @POST("logoutUser")
    fun logoutUserAsync():Deferred<LogoutUserResponse>

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

                val response = it.proceed(request)
                // TODO check response code val responseCode = response.code()
                return@Interceptor response
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