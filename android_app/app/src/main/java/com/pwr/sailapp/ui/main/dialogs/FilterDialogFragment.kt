package com.pwr.sailapp.ui.main.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.pwr.sailapp.R
import com.pwr.sailapp.utils.formatDistance
import com.pwr.sailapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_filter_dialog.*
import java.lang.StringBuilder

// Fragments cannot have constructor arguments
class FilterDialogFragment() : DialogFragment(){

    companion object {
        const val FIVE_STARS = 4.5
        const val FOUR_STARS = 3.5
        const val THREE_STARS = 2.5
        const val NO_MATTER = 0.0
    }


    private lateinit var mainViewModel: MainViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.fragment_filter_dialog, null).also {
                initDialogView(it)})
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initDialogView(view: View){
        val buttonCancel = view.findViewById<Button>(R.id.button_cancel)
        val buttonOk = view.findViewById<Button>(R.id.button_ok)
        val radioGroupStars = view.findViewById<RadioGroup>(R.id.radio_group_stars)
        val maximalDistanceValue = view.findViewById<TextView>(R.id.maximal_distance_value)
        val actualDistanceValue = view.findViewById<TextView>(R.id.actual_distance_value)
        val seekBarDistance = view.findViewById<SeekBar>(R.id.seekBar_distance)

        // Check the previously checked radio button if it had been already checked
        when(mainViewModel.minRating){
            FIVE_STARS -> radioGroupStars.check(R.id.radio_5_stars)
            FOUR_STARS -> radioGroupStars.check(R.id.radio_4_stars)
            THREE_STARS -> radioGroupStars.check(R.id.radio_3_stars)
            else -> radioGroupStars.check(R.id.radio_no_matter)
        }

        maximalDistanceValue.text = formatDistance(mainViewModel.maxDistance)
        actualDistanceValue.text = formatDistance(mainViewModel.actualDistance) // TODO use mutable live data
        seekBarDistance.progress = (100*(mainViewModel.actualDistance/mainViewModel.maxDistance)).toInt()

        seekBarDistance.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mainViewModel.actualDistance = (progress/100.0)*mainViewModel.maxDistance
                actualDistanceValue.text = formatDistance(mainViewModel.actualDistance)
            }
        })

        // Listen to cancel and ok buttons
        buttonCancel.setOnClickListener { dialog?.dismiss() }
        buttonOk.setOnClickListener {
            var minRating = NO_MATTER
            when(radioGroupStars.checkedRadioButtonId){
                R.id.radio_5_stars -> minRating = FIVE_STARS
                R.id.radio_4_stars -> minRating = FOUR_STARS
                R.id.radio_3_stars -> minRating = THREE_STARS
            }
         //   listener.onFilterSelected(minRating)
            mainViewModel.minRating = minRating
        //    mainViewModel.filter() TODO: DON'T use live data to notify on minRating changed
            dialog?.dismiss()
        }
    }
}

