package com.pwr.sailapp.ui.main.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.pwr.sailapp.R
import com.pwr.sailapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_filter_dialog.*
import java.lang.StringBuilder

// Fragments cannot have constructor arguments
class FilterDialogFragment() : DialogFragment(){

    interface OnFilterSelectedListener{
        fun onFilterSelected(minRating: Double)
    }

    lateinit var listener: OnFilterSelectedListener
 //   private lateinit var mainViewModel: MainViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
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
    buttonCancel.setOnClickListener { dialog?.dismiss() }
    buttonOk.setOnClickListener {
        var minRating = 0.0
        when(radioGroupStars.checkedRadioButtonId){
            R.id.radio_5_stars -> minRating = 4.5
            R.id.radio_4_stars -> minRating = 3.5
            R.id.radio_3_stars -> minRating = 2.5
            R.id.radio_no_matter -> minRating = 0.0
        }
        listener.onFilterSelected(minRating)
        dialog?.hide()
    }
}

 //   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
 //       return super.onCreateView(inflater, container, savedInstanceState)
 //   }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try { // fragment to send selected items to
            listener = targetFragment as OnFilterSelectedListener // kotlin casting
        } catch (e: ClassCastException){
            Log.e("FilterDialogFragment", "onAttach: classCastException : "+e.message)
        }
    }
}

