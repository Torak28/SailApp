package com.pwr.sailapp.data.network.sail

import androidx.lifecycle.LiveData
import com.pwr.sailapp.data.network.sail.response.AllCentreGearResponse
import com.pwr.sailapp.data.network.sail.response.AllUserRentalsResponse
import com.pwr.sailapp.data.network.sail.response.CentresResponse
import com.pwr.sailapp.data.sail.Rental
import com.pwr.sailapp.data.sail.User

interface SailNetworkDataSource{
    val downloadedCentres: LiveData<CentresResponse>
    val downloadedAllCentreGear: LiveData<AllCentreGearResponse>
    val downloadedAllUserRentals: LiveData<List<Rental>>
    val downloadedUserData: LiveData<User>

    // updated downloadedCentres
    suspend fun fetchCentres()
    suspend fun fetchAllCentreGear(centreID: Int)
    suspend fun fetchAllUserRentals(authToken: String)
    suspend fun fetchUserData(authToken: String)
}