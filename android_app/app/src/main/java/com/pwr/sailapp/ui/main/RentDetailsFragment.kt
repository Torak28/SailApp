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
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
        mainViewModel.startTime.value = calendar.time

        mainViewModel.startTime.observe(viewLifecycleOwner, Observer {
            val dateFormatted = DateFormat.getDateInstance().format(it)
            textView_choose_date.text = dateFormatted

            val timeFormatted = DateFormat.getTimeInstance().format(it)
            textView_choose_start_time.text = timeFormatted
        })

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

        // Displaying equipment options
        val equipmentArrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, mainViewModel.equipmentOptions)
        mainViewModel.fetchEquipmentOptions()
        equipmentArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_equipment.adapter = equipmentArrayAdapter

        spinner_equipment.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                // Save the position of selected item
                mainViewModel.selectedEquipmentIndex.value = pos
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        spinner_hours.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) { mainViewModel.selectedTimeIndex.value = pos }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Displaying time options
        val timeArrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, mainViewModel.timeOptions)
        mainViewModel.fetchTimeOptions()
        timeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_hours.adapter = timeArrayAdapter

        button_maps.setOnClickListener {
            val location = mainViewModel.selectedCentre.value?.location
            val uri = Uri.parse("geo:0,0?q=$location")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if(intent.resolveActivity(activity!!.packageManager) != null && location != null) startActivity(intent)
            else toast("Cannot launch activity")
        }

        button_call.setOnClickListener {
            val phone = mainViewModel.selectedCentre.value?.phone
            val uri = Uri.parse("tel:$phone")
            val intent = Intent(Intent.ACTION_DIAL, uri)
            if(intent.resolveActivity(activity!!.packageManager) != null && phone != null) startActivity(intent)
            else toast("Cannot launch activity")
        }

        button_confirm.setOnClickListener {
            val confirmationSuccess = mainViewModel.confirmRental()
            if(confirmationSuccess) {
                toast("Confirmed")
                findNavController().navigate(R.id.destination_profile) // navigate to profile after confirmation to view rentals
            }
            else toast("Error")
        }
    }

    // implementation of OnDateSetListener one abstract method interface
    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        // set calendar date according to user's pick
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        mainViewModel.startTime.value = calendar.time
    }

    // implementation of OnTimeSetListener one abstract method interface
    private val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        calendar[Calendar.HOUR] = hourOfDay
        calendar[Calendar.MINUTE] = minute
        mainViewModel.startTime.value = calendar.time
    }

    private fun toast(text : String) = Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
}
