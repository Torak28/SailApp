package com.pwr.sailapp.data.network.sail.response

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    val msg: String? = null,
    val access_token: String? = null
)


