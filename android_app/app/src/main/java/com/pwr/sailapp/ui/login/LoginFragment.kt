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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// https://developer.android.com/guide/navigation/navigation-conditional

const val INVALID_CREDENTIALS = "Invalid email or credentials"

class LoginFragment : LoginScopedFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        button_register.setOnClickListener { navController.navigate(R.id.destination_register) }
        /*
        1. Listen to the login button
        2. If login button clicked
        // show loading bar
        3. start async operation - login user
        4. (a)Wait for the operation
        // hide loading bar
        5. Observe authentication state
            - when Authenticated navigate to profile
            - when Unauthenticated stay here and show error message
        6. Listen to the register button
        7. When register button clicked navigate to RegisterFragment
        */

        button_login.setOnClickListener {
            val email = editText_email.text.toString()
            val password = editText_password.text.toString()
           // loginViewModel.authenticate(editText_email.text.toString(), editText_password.text.toString())

            launch {
                // show loading bar
                val authOperation = async {
                    loginViewModel.authenticate(email, password)
                }
                authOperation.await()
                // hide loading bar

                loginViewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
                    // when is like match or switch-case
                    if(authenticationState == null){
                        Log.e("Login fragment", "loginViewModel.authenticationState.value = null")
                        return@Observer
                    }
                    when(authenticationState){
                        AuthenticationState.AUTHENTICATED -> navController.navigate(R.id.destination_main)
                        else -> Snackbar.make(view,
                            INVALID_CREDENTIALS,
                            Snackbar.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

}
