package com.pwr.sailapp.data.sail

import androidx.lifecycle.LiveData
import com.pwr.sailapp.data.sail.response.AllCentreGearResponse
import com.pwr.sailapp.data.sail.response.CentresResponse

interface SailNetworkDataSource{
    val downloadedCentres: LiveData<CentresResponse>
    val downloadedAllCentreGear: LiveData<AllCentreGearResponse>

    // updated downloadedCentres
    suspend fun fetchCentres()
    suspend fun fetchAllCentreGear(centreID: Int)
}