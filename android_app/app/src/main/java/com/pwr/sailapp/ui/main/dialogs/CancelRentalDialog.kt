package com.pwr.sailapp.ui.main.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.pwr.sailapp.R
import com.pwr.sailapp.viewModel.MainViewModel

class CancelRentalDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)

            builder.setMessage(R.string.cancel_reservation)
                .setPositiveButton(R.string.yes) { dialog, id ->
                    mainViewModel.cancelRental(mainViewModel.currentRental)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.no) { dialog, id -> dialog.dismiss() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}