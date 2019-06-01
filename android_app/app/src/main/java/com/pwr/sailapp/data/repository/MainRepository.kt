package com.pwr.sailapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.RentalSummary
import com.pwr.sailapp.data.network.ResponseStatus
import com.pwr.sailapp.data.sail.Centre
import com.pwr.sailapp.data.sail.Equipment
import com.pwr.sailapp.data.sail.Rental
import com.pwr.sailapp.data.sail.User

interface MainRepository{
    val responseStatus: MutableLiveData<ResponseStatus>
    suspend fun getAllUserRentals(authToken: String): LiveData<ArrayList<Rental>>
    suspend fun getCentres(): LiveData<ArrayList<Centre>>
    suspend fun getAllCentreGear(centreID: Int) : LiveData<ArrayList<Equipment>>
    suspend fun getRentalSummary(rental: Rental) : RentalSummary
    suspend fun getUserData(authToken: String): LiveData<User>
}