package com.pwr.sailapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.pwr.sailapp.R
import com.pwr.sailapp.data.sail.ChangeDataState
import com.pwr.sailapp.data.sail.User
import com.pwr.sailapp.ui.generic.MainScopedFragment
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val INVALID_DATA = "Invalid data"
const val CHANGE_DATA_SUCCESS_MSG = "Changed data successfully"
const val CHANGE_DATA_FAIL_MSG = "Cannot change data"

class SettingsFragment : MainScopedFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.user.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                Log.e("MainAct", "user = null"); return@Observer
            }
            changeDisplayedUserDetails(it)
        })

        textView_expand_change_user_data.setOnClickListener {
            linearLayout_change_user_data.visibility = View.VISIBLE
        }

        button_change_user_data_dismiss.setOnClickListener {
            linearLayout_change_user_data.visibility = View.GONE
        }

        button_change_user_data_confirm.setOnClickListener(onButtonConfirmListener)

    }


    private fun changeDisplayedUserDetails(user: User) {
        textView_first_name_settings.text = user.firstName
        textView_second_name_settings.text = user.lastName
        textView_phone_settings.text = user.phoneNumber
        textView_email_settings.text = user.email
    }

    private val onButtonConfirmListener = View.OnClickListener {
        val firstName = editText_first_name_settings.text.toString()
        val lastName = editText_second_name_settings.text.toString()
        val phoneNumber = editText_phone_settings.text.toString()
        val email = editText_email_settings.text.toString()
        if (mainViewModel.isChangedDataValid(
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                email = email
            )
        ) {
            launch {
                withContext(Dispatchers.IO) {
                    mainViewModel.changeUserData(firstName, lastName, email, phoneNumber)
                }
                mainViewModel.changeDataState.observe(viewLifecycleOwner, Observer {
                    when (it) {
                        ChangeDataState.CHANGE_DATA_SUCCESSFUL -> {
                            snack(CHANGE_DATA_SUCCESS_MSG)
                            launch {
                                withContext(Dispatchers.IO) {mainViewModel.fetchUser()}
                                linearLayout_change_user_data.visibility = View.GONE
                            }
                        }
                        else -> snack(CHANGE_DATA_FAIL_MSG) // hide loading bar
                    }
                })
            }
        } else snack(INVALID_DATA)
    }
}
