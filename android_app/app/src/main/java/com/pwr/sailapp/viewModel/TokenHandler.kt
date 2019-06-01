package com.pwr.sailapp.viewModel

import com.pwr.sailapp.data.repository.NO_TOKEN

object TokenHandler {
    var refreshToken : String = NO_TOKEN
    var accessToken: String = NO_TOKEN
}