package com.pwr.sailapp.data.network.sail.response

import com.google.gson.annotations.SerializedName

data class LoginUserResponse(
    val msg: String? = null,
    val access_token: String? = null,
    val refresh_token: String? = null
)


