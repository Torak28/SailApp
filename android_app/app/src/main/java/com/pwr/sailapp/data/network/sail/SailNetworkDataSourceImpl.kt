package com.pwr.sailapp.data.network.sail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.network.sail.response.AllCentreGearResponse
import com.pwr.sailapp.data.network.sail.response.AllUserRentalsResponse
import com.pwr.sailapp.data.network.sail.response.CentresResponse
import com.pwr.sailapp.data.sail.Rental
import com.pwr.sailapp.data.sail.User
import com.pwr.sailapp.internal.ErrorCodeException
import com.pwr.sailapp.internal.NoConnectivityException

class SailNetworkDataSourceImpl(
    private val sailAppApiService: SailAppApiService
) : SailNetworkDataSource {

    private val _downloadedCentres = MutableLiveData<CentresResponse>()
    private val _downloadedAllCentreGear = MutableLiveData<AllCentreGearResponse>()
    private val _downloadedAllUserRentals = MutableLiveData<List<Rental>>()
    private val _downloadedUserData = MutableLiveData<User>()
    /*
    Client cannot change mutable live data since they receive not mutable live data
     */

    override val downloadedUserData: LiveData<User>
        get() = _downloadedUserData

    override val downloadedCentres: LiveData<CentresResponse>
        get() = _downloadedCentres

    override val downloadedAllCentreGear: LiveData<AllCentreGearResponse>
        get() = _downloadedAllCentreGear

    override val downloadedAllUserRentals: LiveData<List<Rental>>
        get() = _downloadedAllUserRentals


    override suspend fun fetchAllUserRentals(authToken : String) {
        try{
            val bearerToken = "Bearer $authToken"
            val fetchedAllUserRentals = sailAppApiService.getAllUserRentals(bearerToken)
                .await()
            _downloadedAllUserRentals.postValue(fetchedAllUserRentals)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection")
        }
        catch (e: ErrorCodeException){
            Log.e("fetchAllUserRentals", "ErrorCodeException")
        }
    }

    override suspend fun fetchUserData(authToken: String) {
        try{
            val bearerToken = "Bearer $authToken"
            val fetchedUserData = sailAppApiService.getUserDataAsync(bearerToken)
                .await()
            _downloadedUserData.postValue(fetchedUserData)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection")
        }
        catch (e: ErrorCodeException){
            Log.e("fetchUserData", "ErrorCodeException")
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
        catch (e: ErrorCodeException){
            Log.e("fetchCentres", "ErrorCodeException")
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