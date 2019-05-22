package com.pwr.sailapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.pwr.sailapp.R
import com.pwr.sailapp.data.network.sail.ConnectivityInterceptorImpl
import com.pwr.sailapp.data.network.weather.DarkSkyApiService
import com.pwr.sailapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class StatsFragment : Fragment(), CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // val mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        val apiService = DarkSkyApiService(ConnectivityInterceptorImpl(requireContext()))
        launch {

            // mainViewModel.fetchEquipment(1)
            val response = apiService.getForecast(
                "54.692867", "18.691693", "1558617780"
            ).await()
            textView_stats_fragment.text = response.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}

