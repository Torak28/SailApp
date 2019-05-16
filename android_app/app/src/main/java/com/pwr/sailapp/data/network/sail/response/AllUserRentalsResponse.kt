package com.pwr.sailapp.data.network.sail.response

import com.pwr.sailapp.data.Rental

data class AllUserRentalsResponse (
    val rentals: ArrayList<Rental>
)