package com.pwr.sailapp.ui.main


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.pwr.sailapp.R
import com.pwr.sailapp.data.Rental
import com.pwr.sailapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_rent_details.*
import java.text.DateFormat
import java.time.Year
import java.util.*

// https://www.tutorialkart.com/kotlin-android/android-datepicker-kotlin-example/
// TODO use mutableLiveData selectedDay and time in view model
// TODO implement spinners

class RentDetailsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rent_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        mainViewModel.selectedCentre.observe(viewLifecycleOwner, Observer {
            textView_centre_name_confirm.text = it.name
        })

        // Initially display current date and time
        textView_choose_date.text = DateFormat.getDateInstance().format(calendar.time)
        textView_choose_start_time.text = DateFormat.getTimeInstance().format(calendar.time)

        button_choose_date.setOnClickListener {
            // show DatePicker dialog if button clicked
            DatePickerDialog(requireActivity(),
                dateSetListener, // listener to data set
                // Initially set current date
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
                ).show() // show dialog
        }

        button_choose_start_time.setOnClickListener {
            TimePickerDialog(requireActivity(),
                timeSetListener,
                calendar[Calendar.HOUR],
                calendar[Calendar.MONTH],
                true).show()
        }

        button_maps.setOnClickListener {
            val location = mainViewModel.selectedCentre.value?.location
            val uri = Uri.parse("geo:0,0?q=$location")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if(intent.resolveActivity(activity!!.packageManager) != null && location != null) startActivity(intent)
            else Toast.makeText(requireActivity(), "Cannot launch activity", Toast.LENGTH_SHORT).show()
        }

        button_call.setOnClickListener {
            val phone = mainViewModel.selectedCentre.value?.phone
            val uri = Uri.parse("tel:$phone")
            val intent = Intent(Intent.ACTION_DIAL, uri)
            if(intent.resolveActivity(activity!!.packageManager) != null && phone != null) startActivity(intent)
            else Toast.makeText(requireActivity(), "Cannot launch activity", Toast.LENGTH_SHORT).show()
        }

        button_confirm.setOnClickListener {
            val centre = mainViewModel.selectedCentre.value
            // TODO use live data / data binding for date and time
            val date = textView_choose_date.text.toString()
            val time = textView_choose_start_time.text.toString()
            if(centre != null) {
                mainViewModel.confirmRental(centre, date, time)
                Toast.makeText(requireActivity(), "Confirmed", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.destination_profile) // navigate to profile after confirmation to view rentals
            }
            else Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()

        }
    }

    // implementation of OnDateSetListener one abstract method interface
    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        // set calendar date according to user's pick
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        // format the picked data to local date format
        val pickedDate = DateFormat.getDateInstance().format(calendar.time)
        textView_choose_date.text = pickedDate
    }

    // implementation of OnTimeSetListener one abstract method interface
    private val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        calendar[Calendar.HOUR] = hourOfDay
        calendar[Calendar.MINUTE] = minute
        textView_choose_start_time.text = DateFormat.getTimeInstance().format(calendar.time)
    }


}