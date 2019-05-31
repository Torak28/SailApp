package com.pwr.sailapp.data.network.sail

import androidx.lifecycle.LiveData
import com.pwr.sailapp.data.network.sail.response.AllCentreGearResponse
import com.pwr.sailapp.data.network.sail.response.AllUserRentalsResponse
import com.pwr.sailapp.data.network.sail.response.CentresResponse
import com.pwr.sailapp.data.sail.Rental

interface SailNetworkDataSource{
    val downloadedCentres: LiveData<CentresResponse>
    val downloadedAllCentreGear: LiveData<AllCentreGearResponse>
    val downloadedAllUserRentals: LiveData<List<Rental>>

    // updated downloadedCentres
    suspend fun fetchCentres()
    suspend fun fetchAllCentreGear(centreID: Int)
    suspend fun fetchAllUserRentals(authToken: String)
}