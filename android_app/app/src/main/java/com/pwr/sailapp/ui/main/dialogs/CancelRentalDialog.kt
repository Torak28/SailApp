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
            mainViewModel.isCancellationAllowed = false

            builder.setMessage(R.string.cancel_reservation)
                .setPositiveButton(R.string.yes) { dialog, id ->
                    mainViewModel.isCancellationAllowed = true
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.no) { dialog, id ->
                    mainViewModel.isCancellationAllowed = false
                    dialog.dismiss() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}