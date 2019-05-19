package com.pwr.sailapp.ui.login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

import com.pwr.sailapp.R
import com.pwr.sailapp.data.AuthenticationState
import com.pwr.sailapp.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*

// https://developer.android.com/guide/navigation/navigation-conditional

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel::class.java)

        val navController = findNavController()

        // authenticate user when Login button clicked
        button_login.setOnClickListener {
            loginViewModel.authenticate(editText_email.text.toString(), editText_password.text.toString())
        }

        button_register.setOnClickListener {
            navController.navigate(R.id.destination_register)
        }

        loginViewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            // when is like match or switch-case
            when(authenticationState){
                AuthenticationState.AUTHENTICATED -> navController.navigate(R.id.destination_main)
                else -> Snackbar.make(view,
                    "Invalid credentials",
                    Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}
