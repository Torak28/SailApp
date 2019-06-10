package com.pwr.sailapp.data.network.sail

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.pwr.sailapp.data.network.sail.response.*
import com.pwr.sailapp.data.sail.Centre
import com.pwr.sailapp.data.sail.Gear
import com.pwr.sailapp.data.sail.Rental
import com.pwr.sailapp.data.sail.User
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

    // Good one
    @GET("default/refreshToken")
    fun refreshAuthTokenAsync(
        @Body refresh_token: String
    ):Deferred<String>


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

    @GET("accounts/getUserData")
    fun getUserDataAsync(
        @Header("Authorization") authToken: String
    ): Deferred<User>

    @GET("centre/getCentres")
    fun getCentresAsync(
        @Header("Authorization") authToken: String
    ): Deferred<List<Centre>>

    @GET("user/getPicturesOfCentre/{centre_id}")
    fun getPicturesOfCentreAsync(
        @Header("Authorization") authToken: String,
        @Path("centre_id") centreID: Int
    ): Deferred<List<PictureOfCentreResponse>>

    @GET("/user/getPicture/{picture_id}")
    fun getPictureAsync(
        @Header("Authorization") authToken: String,
        @Path("picture_id") centreID: Int
    ): Deferred<PictureOfCentreResponse>

    @GET("gear/getAllGear/{centre_id}")
    fun getAllGearAsync(
        @Header("Authorization") authToken: String,
        @Path("centre_id") centreID: Int
    ): Deferred<List<Gear>>

    @FormUrlEncoded
    @POST("rental/rentGear")
    fun rentGearAsync(
        @Header("Authorization") authToken: String,
        @Field("centre_id") centreID: Int,
        @Field("gear_id") gearID: Int,
        @Field("rent_amount") rentAmount: Int,
        @Field("rent_start") rentStart: String,
        @Field("rent_end") rentEnd : String
    ): Deferred<RentResponse>

    @FormUrlEncoded
    @PUT("rental/cancelRent")
    fun cancelRentAsync(
        @Header("Authorization") authToken: String,
        @Field("rent_id") rentID: Int
    ): Deferred<CancelResponse>

    @GET("getCentres")
    fun getCentres(): Deferred<CentresResponse> // defer - odraczać

    @GET("getAllCentreGear")
    fun getAllCentreGear(
        @Query("centre_id") centreID: Int
    ): Deferred<AllCentreGearResponse>


    @FormUrlEncoded
    @POST("accounts/changeData")
    fun changeDataAsync(
        @Header("Authorization") authToken: String,
        @Field("first_name") firstName : String,
        @Field("last_name") lastName : String,
        @Field("email") email: String,
        @Field("phone_number") phoneNumber : String
    ): Deferred<ChangeDataResponse>



    companion object{
        // syntax: SailAppApiService()
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): SailAppApiService{

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