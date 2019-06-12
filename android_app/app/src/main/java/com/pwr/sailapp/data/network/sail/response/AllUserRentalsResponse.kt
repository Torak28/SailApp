package com.pwr.sailapp.data.network.sail.response

import com.pwr.sailapp.data.sail.Rental

data class AllUserRentalsResponse (
    val msg: String? = null,
    val rentals: ArrayList<Rental>? = null
)