package com.pwr.sailapp.ui.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.pwr.sailapp.R
import com.pwr.sailapp.data.sail.SailAppApiService
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class StatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiService = SailAppApiService()
        GlobalScope.launch(Dispatchers.Main) {
            /*
            val centresResponse = apiService.getCentres().await()
            textView_stats_fragment.text = centresResponse.centres[0].toString()
            */
            val centresResponse = apiService.getAllCentreGear(1).await()
            textView_stats_fragment.text = centresResponse.gear[0].toString()
        }
    }


}
