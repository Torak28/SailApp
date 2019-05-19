package com.pwr.sailapp.data.network.sail.response

import com.google.gson.annotations.SerializedName
import com.pwr.sailapp.data.User

data class LoginUserResponse(
    val message: String,
    //val user: User,
    val email: String, // ??
    @SerializedName("user_id")
    val userID: Int, // ??
    @SerializedName("first_name")
    val firstName: String, // ??
    val token: String
)


