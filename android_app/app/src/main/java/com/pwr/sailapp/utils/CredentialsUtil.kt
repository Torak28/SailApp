package com.pwr.sailapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.pwr.sailapp.data.repository.NO_TOKEN
import com.pwr.sailapp.data.sail.AuthenticationState

// singletone for saving and resetting user credentials
object CredentialsUtil {
    val SHARED_PREF_FILE_NAME = "com.pwr.sailapp.utils.pref"
    val IS_LOGGED_KEY = "com.pwr.sailapp.utils.pref.is_logged"
    val EMAIL_KEY = "com.pwr.sailapp.utils.pref.email"
    val PASSWORD_KEY ="com.pwr.sailapp.utils.pref.password"

    val ACCESS_TOKEN_KEY = "com.pwr.sailapp.utils.pref.accessToken"
    val REFRESH_TOKEN_KEY = "com.pwr.sailapp.utils.pref.refreshToken"
    val AUTH_STATE = "com.pwr.sailapp.utils.pref.authState"

    private fun getSharedPreferences(context: Context): SharedPreferences? {
        // TODO replace PreferenceManager since it is deprecated
        return context.getSharedPreferences(SHARED_PREF_FILE_NAME,Context.MODE_PRIVATE)
    }

    fun saveTokensAndState(context: Context,
                            authToken: String,
                            refreshToken:String,
                            authenticationState: AuthenticationState){
        val preferences = getSharedPreferences(context)
        val editor = preferences?.edit()
        when(authenticationState){
            AuthenticationState.AUTHENTICATED -> {
                editor?.putString(ACCESS_TOKEN_KEY, authToken)
                editor?.putString(REFRESH_TOKEN_KEY, refreshToken)
                editor?.putBoolean(AUTH_STATE, true)
            }
            else -> {
                editor?.clear()
                editor?.apply()
                editor?.putBoolean(AUTH_STATE, false)
            }
        }
        editor?.apply()
    }

    fun getAuthToken(context: Context):String{
        val token = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE).getString(ACCESS_TOKEN_KEY, NO_TOKEN)
        return if(token == null) { Log.e("getAuthToken", "token = null"); NO_TOKEN
        } else token
    }

    fun getRefreshToken(context: Context):String{
        val token = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE).getString(REFRESH_TOKEN_KEY, NO_TOKEN)
        return if(token == null) { Log.e("getAuthToken", "token = null"); NO_TOKEN
        } else token
    }

    fun saveUserCredentials(context: Context,
                 email: String,
                 password:String){
        val preferences = getSharedPreferences(context)
        val editor = preferences?.edit()
        editor?.putString(EMAIL_KEY, email)
        // TODO make password safe
        editor?.putString(PASSWORD_KEY, password)
        editor?.putBoolean(IS_LOGGED_KEY, true)
        editor?.apply()
    }

    // if user logs out then clear his credentials in shared preferences
    fun resetUserCredentials(context: Context){
        val preferences = getSharedPreferences(context)
        val editor = preferences?.edit()
        editor?.clear()
        editor?.apply()
    }

    // check if isLogged flag is set in preferences file
    fun isLogged(context: Context):Boolean{
        return context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
            .getBoolean(IS_LOGGED_KEY, false)
    }
}