package com.pwr.sailapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

// singletone for saving and resetting user credentials
object CredentialsUtil {
    val SHARED_PREF_FILE_NAME = "com.pwr.sailapp.utils.pref"
    val IS_LOGGED_KEY = "com.pwr.sailapp.utils.pref.is_logged"
    val EMAIL_KEY = "com.pwr.sailapp.utils.pref.email"
    val PASSWORD_KEY ="com.pwr.sailapp.utils.pref.password"

    private fun getSharedPreferences(context: Context): SharedPreferences? {
        // TODO replace PreferenceManager since it is deprecated
        return context.getSharedPreferences(SHARED_PREF_FILE_NAME,Context.MODE_PRIVATE)
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