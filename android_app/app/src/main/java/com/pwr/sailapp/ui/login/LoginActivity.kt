package com.pwr.sailapp.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.pwr.sailapp.R
import com.pwr.sailapp.data.DataProvider
import com.pwr.sailapp.viewModel.LoginViewModel
import com.pwr.sailapp.viewModel.MainViewModel
import com.pwr.sailapp.viewModel.getViewModel

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}
