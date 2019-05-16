package com.pwr.sailapp.data.network.sail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.network.sail.response.AllCentreGearResponse
import com.pwr.sailapp.data.network.sail.response.AllUserRentalsResponse
import com.pwr.sailapp.data.network.sail.response.CentresResponse
import com.pwr.sailapp.internal.NoConnectivityException

class SailNetworkDataSourceImpl(
    private val sailAppApiService: SailAppApiService
) : SailNetworkDataSource {


    private val _downloadedCentres = MutableLiveData<CentresResponse>()
    private val _downloadedAllCentreGear = MutableLiveData<AllCentreGearResponse>()
    private val _downloadedAllUserRentals = MutableLiveData<AllUserRentalsResponse>()
    /*
    Client cannot change mutable live data since they receive not mutable live data
     */
    override val downloadedCentres: LiveData<CentresResponse>
        get() = _downloadedCentres

    override val downloadedAllCentreGear: LiveData<AllCentreGearResponse>
        get() = _downloadedAllCentreGear

    override val downloadedAllUserRentals: LiveData<AllUserRentalsResponse>
        get() = _downloadedAllUserRentals


    override suspend fun fetchAllUserRentals(userID: Int) {
        try{
            val fetchedAllUserRentals = sailAppApiService.getAllUserRentals(userID)
                .await()
            _downloadedAllUserRentals.postValue(fetchedAllUserRentals)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection")
        }
    }

    override suspend fun fetchCentres() {
        try{
            val fetchedCentres = sailAppApiService.getCentres()
                .await()
            _downloadedCentres.postValue(fetchedCentres)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection")
        }
    }

    override suspend fun fetchAllCentreGear(centreID: Int) {
        try{
            val fetchedAllCentreGear = sailAppApiService.getAllCentreGear(centreID)
                .await()
            _downloadedAllCentreGear.postValue(fetchedAllCentreGear)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection")
        }
    }

}