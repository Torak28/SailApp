package com.pwr.sailapp.ui.main


import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.pwr.sailapp.R
import com.pwr.sailapp.data.sail.Equipment
import com.pwr.sailapp.ui.generic.MainScopedFragment
import com.pwr.sailapp.utils.formatCoordinate
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener
import kotlinx.android.synthetic.main.fragment_rent_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

// https://www.tutorialkart.com/kotlin-android/android-datepicker-kotlin-example/

const val ON_SUCCESS_TOAST = "Confirmed"
const val ON_FAILURE_TOAST = "Error"

class RentDetailsFragment : MainScopedFragment() {

    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rent_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val equipmentArrayAdapter =
            ArrayAdapter<Equipment>(requireContext(), android.R.layout.simple_spinner_item, ArrayList<Equipment>())
        equipmentArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_equipment.adapter = equipmentArrayAdapter
        /*
        1. Show loading bar - Main thread
        2. Fetch equipment options - IO thread
        3. (a)Wait until equipment is fetched
        4. Hide loading bar
        5. Observe changes in equipment
        6. On change in equipment attach new array adapter to spinner
        7. Do the rest
         */
        launch {
            linearLayout_loading_details.visibility = View.VISIBLE
            linearLayout_details.visibility = View.GONE

            if(mainViewModel.selectedCentre.value != null){
                val operation = async(Dispatchers.IO) {
                    mainViewModel.fetchEquipment(mainViewModel.selectedCentre.value!!.ID) 
                }
                operation.await()
            }
            else Log.e("RentDetailsFragment", "mainViewModel.selectedCentre.value = null" )

            linearLayout_loading_details.visibility = View.GONE
            linearLayout_details.visibility = View.VISIBLE


            mainViewModel.centreEquipment.observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    Log.e("RentDetailsFragment", "centreEquipment.value = null"); return@Observer
                }
                equipmentArrayAdapter.clear()
                equipmentArrayAdapter.addAll(it)
            })


            spinner_equipment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                    // Save the position of selected item
                    mainViewModel.selectedEquipment.value = equipmentArrayAdapter.getItem(pos)// !!!
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }


            // Observe selected centre
            mainViewModel.selectedCentre.observe(viewLifecycleOwner, Observer {
                textView_centre_name_confirm.text = it.name
            })

            // Initially display current date and time
            mainViewModel.startTime.value = calendar.time
            mainViewModel.endTime.value = calendar.time

            // Update start date and time display on start time changed
            mainViewModel.startTime.observe(viewLifecycleOwner, Observer {
                val dateFormatted = DateFormat.getDateInstance().format(it)
                textView_choose_date.text = dateFormatted

                val startTimeFormatted = DateFormat.getTimeInstance().format(it)
                textView_choose_start_time.text = startTimeFormatted
            })

            // Update end time display on start end changed
            mainViewModel.endTime.observe(viewLifecycleOwner, Observer {
                val endTimeFormatted = DateFormat.getTimeInstance().format(it)
                textView_choose_end_time.text = endTimeFormatted
            })

            button_choose_date.setOnClickListener {
                // show DatePicker dialog if button clicked
                DatePickerDialog(
                    requireActivity(),
                    dateSetListener, // listener to data set
                    calendar[Calendar.YEAR],
                    calendar[Calendar.MONTH],
                    calendar[Calendar.DAY_OF_MONTH] // Initially set current date
                ).show() // show dialog
            }

            // Start time dialog
            button_choose_start_time.setOnClickListener {
                val timeDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                    startTimeSetListener, // listener to time set
                    true
                ).apply {
                    setTimeInterval(1, 30)
                    enableSeconds(false)
                }
                fragmentManager?.let { it1 -> timeDialog.show(it1, "Start time dialog") }
            }

            // End time dialog
            button_choose_end_time.setOnClickListener {
                val timeDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                    endTimeSetListener,
                    true
                ).apply {
                    setTimeInterval(1, 30)
                    enableSeconds(false)
                    //setMinTime()
                }
                fragmentManager?.let { it1 -> timeDialog.show(it1, "End time dialog") }
            }


            // Launch maps activity
            button_maps.setOnClickListener {
                launchMaps()
            }

            // Launch dial activity
            button_call.setOnClickListener {
                launchDial()
            }

            // Confirm rental and when it is ok then navigate to user profile
            button_confirm.setOnClickListener {
                val confirmationSuccess = mainViewModel.confirmRental()
                if (confirmationSuccess) {
                    toast(ON_SUCCESS_TOAST)
                    findNavController().navigate(R.id.destination_profile) // navigate to profile after confirmation to view rentals
                } else toast(ON_FAILURE_TOAST)
            }

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
    private val startTimeSetListener = OnTimeSetListener{ _, hourOfDay, minute, second ->
        calendar[Calendar.HOUR] = hourOfDay
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = second
        mainViewModel.startTime.value = calendar.time
    }

    // implementation of OnTimeSetListener one abstract method interface
    private val endTimeSetListener = OnTimeSetListener{ _, hourOfDay, minute, second ->
        calendar[Calendar.HOUR] = hourOfDay
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = second
        mainViewModel.endTime.value = calendar.time
    }


    // launches maps and shows location of a selected centre from the view model
    private fun launchMaps(){
        val coordinateXFormatted = formatCoordinate(mainViewModel.selectedCentre.value!!.coordinateX, 4)
        val coordinateYFormatted = formatCoordinate(mainViewModel.selectedCentre.value!!.coordinateY, 4)
        val label = mainViewModel.selectedCentre.value!!.name
        val uri = Uri.parse("geo:0,0?q=$coordinateXFormatted,$coordinateYFormatted($label)")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(activity!!.packageManager) != null) startActivity(intent)
        else toast("Cannot launch activity")
    }

    // launches dial of a selected centre from the view model
    private fun launchDial(){
        val phone = mainViewModel.selectedCentre.value?.phone
        val uri = Uri.parse("tel:$phone")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        if (intent.resolveActivity(activity!!.packageManager) != null && phone != null) startActivity(intent)
        else toast("Cannot launch activity")
    }

    private fun toast(text: String) = Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
}
