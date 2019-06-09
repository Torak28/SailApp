package com.pwr.sailapp.ui.login


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

import com.pwr.sailapp.R
import com.pwr.sailapp.data.sail.AuthenticationState
import com.pwr.sailapp.ui.generic.LoginScopedFragment
import com.pwr.sailapp.ui.generic.ScopedFragment
import com.pwr.sailapp.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.*

// https://developer.android.com/guide/navigation/navigation-conditional

const val INVALID_CREDENTIALS = "Invalid email or credentials"
const val LOGIN_SUCCESSFUL = "Login successful"

class LoginFragment : LoginScopedFragment() {

    private val navController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_register.setOnClickListener { navController.navigate(R.id.action_loginFragment_to_registerFragment) }

        button_login.setOnClickListener {
            val email = editText_email.text.toString()
            val password = editText_password.text.toString()

            launch {
                changeLoadingBarVisibility(true)
                withContext(Dispatchers.IO) {loginViewModel.authenticate(email, password)}
                changeLoadingBarVisibility(false)
                loginViewModel.authenticationState.observe(viewLifecycleOwner, authenticationStateObserver)
            }
        }
    }

    private val authenticationStateObserver = Observer<AuthenticationState> {
        if (it == null) {
            Log.e("Login fragment", "loginViewModel.authenticationState.value = null")
            return@Observer
        }
        when (it) {
            AuthenticationState.AUTHENTICATED -> {
                snack(LOGIN_SUCCESSFUL)
                navController.navigate(R.id.destination_main)
            }
            else -> snack(INVALID_CREDENTIALS)
        }
    }

    override fun changeLoadingBarVisibility(isVisible: Boolean) {
        super.changeLoadingBarVisibility(isVisible)
        if(isVisible){
            button_login.visibility = View.GONE
            button_register.visibility = View.GONE
            linearLayout_logging.visibility = View.VISIBLE
        }
        else{
            button_login.visibility = View.VISIBLE
            button_register.visibility = View.VISIBLE
            linearLayout_logging.visibility = View.GONE
        }
    }
}
