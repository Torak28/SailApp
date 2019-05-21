package com.pwr.sailapp.data.repository

import androidx.lifecycle.LiveData
import com.pwr.sailapp.data.sail.Centre
import com.pwr.sailapp.data.sail.Equipment
import com.pwr.sailapp.data.sail.Rental

interface MainRepository{
    suspend fun getCentres(): LiveData<ArrayList<Centre>>
    suspend fun getAllUserRentals(userID: Int): LiveData<ArrayList<Rental>>
    suspend fun getAllCentreGear(centreID: Int) : LiveData<ArrayList<Equipment>>
}