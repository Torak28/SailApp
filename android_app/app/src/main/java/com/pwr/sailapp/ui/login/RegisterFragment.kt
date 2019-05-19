package com.pwr.sailapp.ui.login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

import com.pwr.sailapp.R
import com.pwr.sailapp.ui.main.ScopedFragment
import com.pwr.sailapp.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class RegisterFragment : ScopedFragment() {

    private lateinit var navController: NavController
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel::class.java)
        navController = findNavController()

        /*
        1. Listen to register button
        2. When button clicked validate input data
        3. If data is not correct show massage - Main thread
        // show loading bar
        4. If data is correct launch registration - IO thread
        5. Await for the registration result - Main thread
        // hide loading bar
        6. If result = "ok" -> toast("Registration successful"); navigate to login fragment
        7. If result = "error" -> error message = VISIBLE
        */

        button_confirm_registration.setOnClickListener {
            textView_registration_error.visibility = View.GONE
            val firstName = editText_first_name.text.toString()
            val lastName = editText_second_name.text.toString()
            val phoneNumber = editText_phone.text.toString()
            val email = editText_email_register.text.toString()
            val password = editText_password_register.text.toString()
            val confirmedPassword = editText_confirm_password_register.text.toString()
            val hasAgreed = checkBox_licence_agreement.isChecked
            val isDataCorrect = loginViewModel.validateRegistrationData(firstName, lastName, phoneNumber, email, password, confirmedPassword, hasAgreed)
            if(!isDataCorrect){
                // show information about incorrect data
                Toast.makeText(requireContext(), "Incorrect data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            launch {
                // Show loading bar
                val registerOperationAsync = async {
                    // postUserDataToServer and wait for response
                }
                registerOperationAsync.await()
                // Hide loading bar
                // val message : String = loginViewModel.message // Live data ???
                // if(message = "ok") navController -> navigate(login)
                // else textView_registration_error.visibility = View.VISIBLE
            }


        }
    }


}
