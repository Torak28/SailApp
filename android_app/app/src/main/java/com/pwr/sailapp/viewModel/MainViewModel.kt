package com.pwr.sailapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.Centre
import com.pwr.sailapp.utils.CredentialsUtil
import com.pwr.sailapp.data.MockCentres
import com.pwr.sailapp.data.Rental

/*
https://developer.android.com/guide/navigation/navigation-conditional#kotlin
https://developer.android.com/reference/android/arch/lifecycle/AndroidViewModel - AndroidViewModel provides application (and its context!)
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    enum class AuthenticationState{
        AUTHENTICATED,
        UNAUTHENTICATED,
        INVALID_AUTHENTICATION
    }

    val authenticationState = MutableLiveData<AuthenticationState>() // observe it to know if user is logged in
    val centres = MutableLiveData<ArrayList<Centre>>() // observe it to know which centres are available
    val selectedCentre = MutableLiveData<Centre>() // observe which centre was selected
    val rentals = MutableLiveData<ArrayList<Rental>>()

    init{
        // TODO use repository and LiveData here
        if (CredentialsUtil.isLogged(application.applicationContext))  authenticationState.value = AuthenticationState.AUTHENTICATED
        else authenticationState.value = AuthenticationState.UNAUTHENTICATED
        centres.value = MockCentres.centres
        rentals.value = ArrayList<Rental>()
    }

    // TODO consider using live data for observing whether the user has remover their credentials from shared preferences (logged out)

    fun selectCentre(centre: Centre){ // TODO consider nicer Kotlin syntax
        selectedCentre.value = centre
    }

    fun confirmRental(centre: Centre, startDate: String, startTime: String){
    //    val rentalsPrev = rentals.value
    //    rentalsPrev?.add(Rental(centre, startDate, startTime))
        rentals.value?.add(Rental(centre, startDate, startTime))
    }

    fun logOut(){
        CredentialsUtil.resetUserCredentials(appContext)
        authenticationState.value = MainViewModel.AuthenticationState.UNAUTHENTICATED
    }

}