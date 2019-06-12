package com.pwr.sailapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.sail.AuthenticationState
import com.pwr.sailapp.data.network.sail.ConnectivityInterceptorImpl
import com.pwr.sailapp.data.network.sail.SailAppApiService
import com.pwr.sailapp.data.network.sail.response.REGISTER_OK_MESSAGE
import com.pwr.sailapp.data.sail.RegistrationState
import com.pwr.sailapp.data.sail.User
import com.pwr.sailapp.internal.ErrorCodeException
import com.pwr.sailapp.internal.NoConnectivityException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*
https://developer.android.com/guide/navigation/navigation-conditional#kotlin
 */


class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    private val sailAppApiService = SailAppApiService(connectivityInterceptor = ConnectivityInterceptorImpl(appContext))


    val authenticationState = MutableLiveData<AuthenticationState>()
    val registrationState = MutableLiveData<RegistrationState>()

    // Authentication
    suspend fun authenticate(email: String, password: String) = doNetworkOperation {
        val tokensDeferred = sailAppApiService.loginUserAsync(email, password)
        val tokensRes = tokensDeferred.await()
        if(tokensRes.access_token != null && tokensRes.refresh_token != null){
            TokenHandler.accessToken = tokensRes.access_token
            TokenHandler.refreshToken = tokensRes.refresh_token
            authenticationState.postValue(AuthenticationState.AUTHENTICATED)
        }
        else{
            Log.e("authenticate","tokensRes.access_token = null or tokensRes.refresh_token = null")
            authenticationState.postValue(AuthenticationState.UNAUTHENTICATED)
        }
    }


    suspend fun registerUser(user: User) = doNetworkOperation {
        val msgDeferred = sailAppApiService.registerUserAsync(
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            phoneNumber = user.phoneNumber,
            password = user.password,
            role = "user"
        )
        val msgRes = msgDeferred.await()
        if(msgRes.msg == REGISTER_OK_MESSAGE) registrationState.postValue(RegistrationState.OK)
        else registrationState.postValue(RegistrationState.FAILED)
    }

    fun validateRegistrationData(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        email: String,
        password: String,
        confirmedPassword: String,
        hasAgreed: Boolean
    ): Boolean {
        return firstName.isNotEmpty() &&
                lastName.isNotEmpty() &&
                phoneNumber.isNotEmpty() &&
                email.isNotEmpty() &&
                password.isNotEmpty() &&
                password == confirmedPassword &&
                hasAgreed
    }


    private suspend fun doNetworkOperation(
        logic: suspend () -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                logic()
            } catch (e: NoConnectivityException) {
                Log.e("fetchUser", "No connectivity exception")
            } catch (e: ErrorCodeException) {
                when (e.code) {
                    401 ->{
                        authenticationState.postValue(AuthenticationState.UNAUTHENTICATED)
                        registrationState.postValue(RegistrationState.FAILED)
                    }
                }
                Log.e("fetchUser", "Error Code exception")
            }
        }
    }

}