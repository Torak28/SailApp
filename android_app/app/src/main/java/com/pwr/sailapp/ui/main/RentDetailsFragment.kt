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
import androidx.navigation.fragment.findNavController

import com.pwr.sailapp.R
import com.pwr.sailapp.data.sail.Gear
import com.pwr.sailapp.data.sail.RentalState
import com.pwr.sailapp.ui.generic.MainScopedFragment
import com.pwr.sailapp.utils.formatCoordinate
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener
import kotlinx.android.synthetic.main.fragment_rent_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.util.*

// https://www.tutorialkart.com/kotlin-android/android-datepicker-kotlin-example/

const val SUCCESS_MESSAGE = "Confirmed"
const val FAILURE_MESSAGE = "Error"
const val NO_GEAR_MSG = "This centre has no gear"

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

        launch {
            changeLoadingBarVisibility(isVisible = true)

            withContext(Dispatchers.IO) {
                mainViewModel.fetchGear()
            }

            changeLoadingBarVisibility(isVisible = false)


            val gearArrayAdapter =
                ArrayAdapter<Gear>(requireContext(), android.R.layout.simple_spinner_item, mainViewModel.gearList)
            gearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_equipment.adapter = gearArrayAdapter

            numberPicker.maxValue = mainViewModel.rentAmountLimit
            spinner_equipment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                    if (gearArrayAdapter.getItem(pos) == null) {
                        Log.e("spinner_equipment", "gearArrayAdapter.getItem(pos) == null");return
                    }
                    val selectedGear = gearArrayAdapter.getItem(pos)!!
                    mainViewModel.selectedGearId = selectedGear.ID
                    mainViewModel.rentAmountLimit = selectedGear.quantity
                    numberPicker.maxValue = mainViewModel.rentAmountLimit
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            showInitialDates()

            textView_centre_name_confirm.text = mainViewModel.selectedCentre.name

            // Date picker
            button_choose_date.setOnClickListener(onDateClickListener)

            // Start time picker
            button_choose_start_time.setOnClickListener(onStartTimeClickListener)

            // End time picker
            button_choose_end_time.setOnClickListener(onEndTimeClickListener)

            // Launch maps activity
            button_maps.setOnClickListener { launchMaps() }

            // Launch dial activity
            button_call.setOnClickListener { launchDial() }

            // Confirm rental and when it is ok then navigate to user profile
            button_confirm.setOnClickListener(onConfirmClickListener)

            if(mainViewModel.gearList.isEmpty()){
                snack(NO_GEAR_MSG)
                findNavController().navigate(R.id.destination_rent_master)
            }
        }
    }

    // implementation of OnDateSetListener one abstract method interface
    private val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        // set calendar date according to user's pick
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        calendar.time.let {
            mainViewModel.apply {
                rentStart = it
                rentEnd = it
            }
        }
        val dateFormatted = DateFormat.getDateInstance().format(mainViewModel.rentStart)
        textView_choose_date.text = dateFormatted
    }

    // implementation of OnTimeSetListener one abstract method interface
    private val startTimeSetListener = OnTimeSetListener { _, hourOfDay, minute, second ->
        calendar[Calendar.HOUR] = hourOfDay
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = second
        mainViewModel.rentStart = calendar.time
        val dateFormatted = DateFormat.getTimeInstance().format(mainViewModel.rentStart)
        textView_choose_start_time.text = dateFormatted
    }

    // implementation of OnTimeSetListener one abstract method interface
    private val endTimeSetListener = OnTimeSetListener { _, hourOfDay, minute, second ->
        calendar[Calendar.HOUR] = hourOfDay
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = second
        mainViewModel.rentEnd = calendar.time
        val dateFormatted = DateFormat.getTimeInstance().format(mainViewModel.rentEnd)
        textView_choose_end_time.text = dateFormatted
    }

    private val onDateClickListener = View.OnClickListener {
        // show DatePicker dialog if button clicked
        calendar.time = mainViewModel.rentStart
        DatePickerDialog(
            requireActivity(),
            dateSetListener, // listener to data set
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH] // Initially set current date
        ).show() // show dialog
    }

    private val onStartTimeClickListener = View.OnClickListener {
        calendar.time = mainViewModel.rentStart
        val timeDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
            startTimeSetListener, // listener to time set
            true
        ).apply {
            setTimeInterval(1, 30)
            enableSeconds(false)
        }
        fragmentManager?.let { it1 -> timeDialog.show(it1, "Start time dialog") }
    }

    private val onEndTimeClickListener = View.OnClickListener {
        calendar.time = mainViewModel.rentEnd
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

    private val onConfirmClickListener = View.OnClickListener {
        mainViewModel.rentAmount = numberPicker.value
        launch {
            changeLoadingBarVisibility(true)
            withContext(Dispatchers.IO) {
                mainViewModel.rentGear()
            }
            mainViewModel.rentalState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                changeLoadingBarVisibility(false)
                when (it) {
                    RentalState.RENTAL_SUCCESSFUL -> {
                        toast(SUCCESS_MESSAGE)
                        findNavController().navigate(R.id.destination_profile)
                    }
                    else -> snack(FAILURE_MESSAGE)
                }
            })
        }
    }


    // launches maps and shows location of a selected centre from the view model
    private fun launchMaps() {
        val coordinateXFormatted = formatCoordinate(mainViewModel.selectedCentre.coordinateX, 4)
        val coordinateYFormatted = formatCoordinate(mainViewModel.selectedCentre.coordinateY, 4)
        val label = mainViewModel.selectedCentre.name
        val uri = Uri.parse("geo:0,0?q=$coordinateXFormatted,$coordinateYFormatted($label)")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(activity!!.packageManager) != null) startActivity(intent)
        else toast("Cannot launch activity")
    }

    // launches dial of a selected centre from the view model
    private fun launchDial() {
        val phone = mainViewModel.selectedCentre.phone
        val uri = Uri.parse("tel:$phone")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        if (intent.resolveActivity(activity!!.packageManager) != null) startActivity(intent)
        else toast("Cannot launch activity")
    }

    override fun changeLoadingBarVisibility(isVisible: Boolean) {
        super.changeLoadingBarVisibility(isVisible)
        if (isVisible) {
            // show loading bar
            linearLayout_loading_details.visibility = View.VISIBLE
            button_confirm.visibility = View.GONE
        } else {
            // hide loading bar
            linearLayout_loading_details.visibility = View.GONE
            button_confirm.visibility = View.VISIBLE
        }
    }

    private fun showInitialDates() {
        val dateFormatted = DateFormat.getDateInstance().format(mainViewModel.rentStart)
        textView_choose_date.text = dateFormatted

        val startTimeFormatted = DateFormat.getTimeInstance().format(mainViewModel.rentStart)
        textView_choose_start_time.text = startTimeFormatted

        val endTimeFormatted = DateFormat.getTimeInstance().format(mainViewModel.rentEnd)
        textView_choose_end_time.text = endTimeFormatted
    }
}
