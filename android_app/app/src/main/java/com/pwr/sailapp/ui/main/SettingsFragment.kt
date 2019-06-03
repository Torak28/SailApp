package com.pwr.sailapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.pwr.sailapp.R
import com.pwr.sailapp.data.sail.User
import com.pwr.sailapp.ui.generic.MainScopedFragment
import kotlinx.android.synthetic.main.fragment_settings.*


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
            if (it == null) { Log.e("MainAct", "user = null"); return@Observer }
            changeDisplayedUserDetails(it)
        })

        textView_expand_change_user_data.setOnClickListener {
            linearLayout_change_user_data.visibility = View.VISIBLE
        }

        button_change_user_data_dismiss.setOnClickListener{
            linearLayout_change_user_data.visibility = View.GONE
        }

        button_change_user_data_confirm.setOnClickListener {
            // do async change data
        }

    }


    private fun changeDisplayedUserDetails(user : User){
        textView_first_name_settings.text = user.firstName
        textView_second_name_settings.text = user.lastName
        textView_phone_settings.text = user.phoneNumber
        textView_email_settings.text = user.email
    }
}
