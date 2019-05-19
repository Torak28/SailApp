package com.pwr.sailapp.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.pwr.sailapp.R
import com.pwr.sailapp.data.Rental
import com.pwr.sailapp.ui.main.adapters.RentalAdapter
import com.pwr.sailapp.ui.main.dialogs.CancelRentalDialog
import com.pwr.sailapp.utils.formatCoordinate
import com.pwr.sailapp.data.AuthenticationState
import com.pwr.sailapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

// TODO consider using generic adapter for view model

class ProfileFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_rentals.layoutManager = LinearLayoutManager(context!!)
        val adapter = RentalAdapter(context!!, phoneListener = onRentalCall(), locationListener = onRentalMap(), cancelListener = onRentalCancel())
        recyclerView_rentals.adapter = adapter

        /*
         Use ViewModelProviders to get the same instance and of(activity) to get context of activity - not a single fragment
         of(//activity) means: viewModel is scoped to activity lifecycle. When activity gets destroyed - viewModel too
        */
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)

        mainViewModel.rentals.observe(viewLifecycleOwner, Observer {
            adapter.setRentals(it)
            // Show list of rentals or info about no rentals
            if(it.size > 0) {
                imageView_no_rentals.visibility = View.GONE
                textView_no_rentals.visibility = View.GONE
                recyclerView_rentals.visibility = View.VISIBLE
            }
            else {
                imageView_no_rentals.visibility = View.VISIBLE
                textView_no_rentals.visibility = View.VISIBLE
                recyclerView_rentals.visibility = View.GONE
            }
        } )

    }

    private fun showWelcomeMessage() = Toast.makeText(requireContext(), "Welcome to your profile!", Toast.LENGTH_SHORT).show()

    private fun onRentalCancel() = {rental:Rental ->
            mainViewModel.currentRental = rental
        val cancelRentalDialog = CancelRentalDialog()
        fragmentManager.let {cancelRentalDialog.show(it!!, "Sort dialog") }
    }

    private fun onRentalCall() = {rental:Rental ->
        val phone = rental.centre.phone
        val uri = Uri.parse("tel:$phone")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        if(intent.resolveActivity(activity!!.packageManager) != null) startActivity(intent)
        else toast("Cannot launch activity")
    }

    private fun onRentalMap() = {rental:Rental ->
        val coordinateXFormatted = formatCoordinate(rental.centre.coordinateX, 4)
        val coordinateYFormatted = formatCoordinate(rental.centre.coordinateY, 4)
        val label = rental.centre.name
        val uri = Uri.parse("geo:0,0?q=$coordinateXFormatted,$coordinateYFormatted($label)")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if(intent.resolveActivity(activity!!.packageManager) != null) startActivity(intent)
        else toast("Cannot launch activity")
    }

    private fun toast(text : String) = Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
}
