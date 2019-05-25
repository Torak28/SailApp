package com.pwr.sailapp.ui.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

import com.pwr.sailapp.R
import com.pwr.sailapp.data.network.sail.ConnectivityInterceptorImpl
import com.pwr.sailapp.data.network.weather.DarkSkyApiService
import com.pwr.sailapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlinx.coroutines.*
import java.text.NumberFormat
import kotlin.coroutines.CoroutineContext
import com.anychart.chart.common.dataentry.ValueDataEntry
import org.joda.time.Months


class StatsFragment : ScopedFragment() {

    val MONTHS: Array<String> = arrayOf("January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        linearLayout_stats_loading.visibility = View.VISIBLE

        launch {

            withContext(Dispatchers.Default) { mainViewModel.fetchStats(userID = 1) }
            mainViewModel.rentalHistory.observe(viewLifecycleOwner, Observer {
           //     textView_stats_fragment.text = it.toString()
                val isDataPrepared = mainViewModel.prepareGraphData()
                if(isDataPrepared){
                    plotBarChart(mainViewModel.rentalNumbers)
                    stats_chart.visibility = View.VISIBLE
                    imageView_anchor.visibility = View.GONE
                    textView_no_sailing_this_year.visibility = View.GONE
                }
                else {
                    stats_chart.visibility = View.GONE
                    imageView_anchor.visibility = View.VISIBLE
                    textView_no_sailing_this_year.visibility = View.VISIBLE
                }
                linearLayout_stats_loading.visibility = View.GONE

                textView_rentals_number.text = it.size.toString()
            })


        }
    }

    private fun plotBarChart(rentalNumbers: ArrayList<Int>){
        val bar = AnyChart.bar()
        val data = ArrayList<DataEntry>()
        for ((i, rentalNumber) in rentalNumbers.withIndex()) {
            data.add(ValueDataEntry(MONTHS[i], rentalNumber))
        }
        bar.data(data)
       // bar.xAxis(true).title("Number of rentals")
        bar.yScale().ticks().allowFractional(false)
        stats_chart.setChart(bar)
    }

}

