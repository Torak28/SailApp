package com.pwr.sailapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.pwr.sailapp.data.sail.AuthenticationState
import com.pwr.sailapp.data.network.sail.ConnectivityInterceptorImpl
import com.pwr.sailapp.data.network.sail.SailAppApiService
import com.pwr.sailapp.data.repository.UserManager
import com.pwr.sailapp.data.repository.UserManagerImpl
import com.pwr.sailapp.data.sail.RegistrationState
import com.pwr.sailapp.data.sail.User
import com.pwr.sailapp.utils.CredentialsUtil

/*
https://developer.android.com/guide/navigation/navigation-conditional#kotlin
 */


class LoginViewModel(
    application: Application,
    private val userManager: UserManager
) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    lateinit var authenticationState : LiveData<AuthenticationState>
    lateinit var registrationState: LiveData<RegistrationState>

    // Authentication
    suspend fun authenticate(email: String, password:String){
        userManager.loginUser(email, password)
        authenticationState = userManager.authStatus
    }

    suspend fun registerUser(user: User){
        userManager.registerUser(user)
        registrationState = userManager.registerStatus
    }

    fun validateRegistrationData(firstName:String,
                                 lastName:String,
                                 phoneNumber:String,
                                 email:String,
                                 password: String,
                                 confirmedPassword: String,
                                 hasAgreed:Boolean ):Boolean
    {
        return firstName.isNotEmpty() &&
                lastName.isNotEmpty() &&
                phoneNumber.isNotEmpty() &&
                email.isNotEmpty() &&
                password.isNotEmpty() &&
                password == confirmedPassword &&
                hasAgreed
    }


}