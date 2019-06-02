package com.pwr.sailapp.ui.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

import com.pwr.sailapp.R
import com.pwr.sailapp.data.sail.RegistrationState
import com.pwr.sailapp.data.sail.User
import com.pwr.sailapp.ui.generic.LoginScopedFragment
import com.pwr.sailapp.ui.generic.ScopedFragment
import com.pwr.sailapp.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.*

const val INCORRECT_DATA = "Incorrect data"
const val CORRECT_REGISTRATION = "Registered successfully"
const val INCORRECT_REGISTRATION = "User already exists"

class RegisterFragment : LoginScopedFragment() {

    private val navController by lazy{
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_confirm_registration.setOnClickListener {
            textView_registration_error.visibility = View.GONE
            if (!isDataCorrect()) {
                snack(INCORRECT_DATA)
                return@setOnClickListener
            }
            val userToRegister = createUser()

            launch {
                changeLoadingBarVisibility(true)
                withContext(Dispatchers.IO) {
                    loginViewModel.registerUser(userToRegister)
                }
                changeLoadingBarVisibility(false)

                loginViewModel.registrationState.observe(viewLifecycleOwner, registrationStateObserver)
            }
        }

    }

    private fun isDataCorrect(): Boolean {
        val firstName = editText_first_name.text.toString()
        val lastName = editText_second_name.text.toString()
        val phoneNumber = editText_phone.text.toString()
        val email = editText_email_register.text.toString()
        val password = editText_password_register.text.toString()
        val confirmedPassword = editText_confirm_password_register.text.toString()
        val hasAgreed = checkBox_licence_agreement.isChecked

        return loginViewModel.validateRegistrationData(
            firstName,
            lastName,
            phoneNumber,
            email,
            password,
            confirmedPassword,
            hasAgreed
        )
    }

    private fun createUser(): User {
        val firstName = editText_first_name.text.toString()
        val lastName = editText_second_name.text.toString()
        val phoneNumber = editText_phone.text.toString()
        val email = editText_email_register.text.toString()
        val password = editText_password_register.text.toString()
        return User(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            phoneNumber = phoneNumber,
            role = "user"
        )
    }

    private val registrationStateObserver = Observer<RegistrationState> {
        when (it) {
            RegistrationState.OK -> {
                snack(CORRECT_REGISTRATION)
                navController.navigate(R.id.destination_login_fragment)
            }
            else -> {
                snack(INCORRECT_REGISTRATION)
                textView_registration_error.visibility = View.VISIBLE
            }
        }
    }

    override fun changeLoadingBarVisibility(isVisible: Boolean) {
        super.changeLoadingBarVisibility(isVisible)
        if(isVisible){
            button_confirm_registration.visibility = View.GONE
            linearLayout_registering.visibility = View.VISIBLE
        }
        else{
            linearLayout_registering.visibility = View.GONE
            button_confirm_registration.visibility = View.VISIBLE
        }
    }


}
