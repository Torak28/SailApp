package com.pwr.sailapp.data.network.sail

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.pwr.sailapp.data.network.sail.response.*
import com.pwr.sailapp.data.sail.Rental
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/*
getCentres: https://0e4682b3-c081-4689-b8c5-51f3f0a7ae09.mock.pstmn.io/getCentres
getAllCentreGear: https://0e4682b3-c081-4689-b8c5-51f3f0a7ae09.mock.pstmn.io/getAllCentreGear?centre_id=1
 */

const val SERVER_URL = "https://projekt-gospodarka-backend.herokuapp.com/"

interface SailAppApiService {

    @FormUrlEncoded
    @POST("accounts/login")
    fun loginUserAsync(
        @Field("email") email: String,
        @Field("password") password: String
    ):Deferred<LoginUserResponse>


    @GET("default/refreshToken")
    fun refreshTokenAsync(
        @Body refresh_token: String
    ):Deferred<LoginUserResponse>


    @FormUrlEncoded
    @POST("accounts/register")
    fun registerUserAsync(
        @Field("first_name") firstName : String,
        @Field("last_name") lastName : String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone_number") phoneNumber : String,
        @Field("role") role : String
    ):Deferred<RegisterUserResponse>

    @GET("/gear/getMyRentedGear")
    fun getAllUserRentals(
        @Header("Authorization") authToken: String
    ):Deferred<List<Rental>>

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
    /*        val requestInterceptor = Interceptor{
                val request = it.request()
                    .newBuilder()
                    .build()

                val response = it.proceed(request)
                // TODO check response code val responseCode = response.code()
                return@Interceptor response
            }

            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
*/
            val okHttpClient = OkHttpClient.Builder()
                //.addInterceptor(loggingInterceptor)
                .addInterceptor(ErrorHandlingInterceptor())
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