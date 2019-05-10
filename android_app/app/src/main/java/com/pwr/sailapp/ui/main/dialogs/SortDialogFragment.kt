package com.pwr.sailapp.ui.main.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.pwr.sailapp.R
import com.pwr.sailapp.utils.formatDistance
import com.pwr.sailapp.viewModel.MainViewModel

class SortDialogFragment : DialogFragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.fragment_sort_dialog, null).also {
                initDialogView(it)})
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initDialogView(view: View){
        val buttonCancel = view.findViewById<Button>(R.id.button_cancel)
        val buttonOk = view.findViewById<Button>(R.id.button_ok)
        val radioGroupSort = view.findViewById<RadioGroup>(R.id.radio_group_sort)

        // Check the previously checked radio button if it had been already checked
        if(mainViewModel.isByRating) radioGroupSort.check(R.id.radio_by_rating)
            else radioGroupSort.check(R.id.radio_by_distance)

        // Listen to cancel and ok buttons
        buttonCancel.setOnClickListener { dialog?.dismiss() }
        buttonOk.setOnClickListener {
            when(radioGroupSort.checkedRadioButtonId){
                R.id.radio_by_rating-> mainViewModel.isByRating = true
                else -> mainViewModel.isByRating = false
            }
            mainViewModel.sort()
            dialog?.dismiss()
        }
    }

}