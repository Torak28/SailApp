package com.pwr.sailapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry

import com.pwr.sailapp.R
import com.pwr.sailapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlinx.coroutines.*
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.pwr.sailapp.ui.generic.MainScopedFragment
import com.pwr.sailapp.data.sail.Rental

class StatsFragment : MainScopedFragment() {

    private val MONTHS: Array<String> = arrayOf(
        "January",
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
        "December"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launch {
            changeLoadingBarVisibility(true)
            withContext(Dispatchers.IO){mainViewModel.fetchRentals()}
            changeLoadingBarVisibility(false)
            mainViewModel.rentals.observe(viewLifecycleOwner, rentalsObserver)
        }
    }


    private fun plotPieChart(rentals: List<Rental>){
        val centreRentalsMap = rentals.groupBy { it.centreName }
        val centreFrequencyMap = centreRentalsMap.mapValues {
            it.value.count()
        }
        val pie = AnyChart.pie()
        val data = ArrayList<DataEntry> ()
        for(entry in centreFrequencyMap){
            data.add(ValueDataEntry(entry.key, entry.value))
        }
        pie.data(data)
        stats_chart.setChart(pie)
    }

    private fun plotBarChart(rentalNumbers: ArrayList<Int>) {
        val bar = AnyChart.bar()
        val data = ArrayList<DataEntry>()
        for ((i, rentalNumber) in rentalNumbers.withIndex()) {
            data.add(ValueDataEntry(MONTHS[i], rentalNumber))
        }
        bar.data(data)
        bar.yScale().ticks().allowFractional(false)
        stats_chart.setChart(bar)
    }

    override fun changeLoadingBarVisibility(isVisible: Boolean) {
        super.changeLoadingBarVisibility(isVisible)
        if (isVisible) {
            linearLayout_stats_loading.visibility = View.VISIBLE
        } else {
            linearLayout_stats_loading.visibility = View.GONE
        }
    }

    private val rentalsObserver = Observer<List<Rental>>{
        plotPieChart(it)
        textView_rentals_number.text = it.size.toString()
    }

}

