package com.pwr.sailapp.data.network.sail

import androidx.lifecycle.LiveData
import com.pwr.sailapp.data.network.sail.response.AllCentreGearResponse
import com.pwr.sailapp.data.network.sail.response.AllUserRentalsResponse
import com.pwr.sailapp.data.network.sail.response.CentresResponse

interface SailNetworkDataSource{
    val downloadedCentres: LiveData<CentresResponse>
    val downloadedAllCentreGear: LiveData<AllCentreGearResponse>
    val downloadedAllUserRentals: LiveData<AllUserRentalsResponse>

    // updated downloadedCentres
    suspend fun fetchCentres()
    suspend fun fetchAllCentreGear(centreID: Int)
    suspend fun fetchAllUserRentals(userID: Int)
}