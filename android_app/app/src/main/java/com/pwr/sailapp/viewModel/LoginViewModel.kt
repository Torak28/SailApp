package com.pwr.sailapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.AuthenticationState
import com.pwr.sailapp.data.mocks.MockUserAuthentication
import com.pwr.sailapp.data.network.sail.ConnectivityInterceptorImpl
import com.pwr.sailapp.data.network.sail.SailAppApiService
import com.pwr.sailapp.data.repository.UserManagerImpl
import com.pwr.sailapp.utils.CredentialsUtil

/*
https://developer.android.com/guide/navigation/navigation-conditional#kotlin
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    // TODO use dependency injection
    private val userManagerImp = UserManagerImpl(SailAppApiService(ConnectivityInterceptorImpl(appContext)))

    lateinit var authenticationState : LiveData<AuthenticationState>

    // Authentication
    suspend fun authenticate(email: String, password:String){
        userManagerImp.loginUser(email, password)
        authenticationState = userManagerImp.authStatus
    }

    fun validateRegistrationData(firstName:String,
                                 lastName:String,
                                 phoneNumber:String,
                                 email:String, password:
                                 String, confiredPassword:
                                 String,
                                 hasAgreed:Boolean ) = true
    // TODO validate input data

}