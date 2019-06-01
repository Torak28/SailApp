package com.pwr.sailapp.data.network.sail.response

import com.google.gson.annotations.SerializedName

data class LoginUserResponse(
    val access_token: String? = null,
    val refresh_token: String? = null,
    val role: String? = null
)


