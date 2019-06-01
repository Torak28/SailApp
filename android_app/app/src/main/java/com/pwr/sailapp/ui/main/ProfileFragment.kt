package com.pwr.sailapp.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pwr.sailapp.R
import com.pwr.sailapp.data.sail.Rental
import com.pwr.sailapp.ui.generic.MainScopedFragment
import com.pwr.sailapp.ui.main.adapters.RentalSummaryAdapter
import com.pwr.sailapp.ui.main.dialogs.CancelRentalDialog
import com.pwr.sailapp.utils.formatCoordinate
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.pwr.sailapp.ui.main.adapters.RentalAdapter
import kotlinx.coroutines.runBlocking

// TODO consider using generic adapter for view model

class ProfileFragment : MainScopedFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private val adapter by lazy {
        RentalAdapter(
            requireContext(),
            phoneListener = onRentalCall(),
            locationListener = onRentalMap(),
            cancelListener = onRentalCancel()
        ).apply { setRentals(ArrayList()) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_rentals.layoutManager = LinearLayoutManager(context!!)
        recyclerView_rentals.adapter = adapter

        launch {
            changeLoadingBarVisibility(isVisible = true)

            withContext(Dispatchers.IO) {
                mainViewModel.fetchRentals()
            }

            changeLoadingBarVisibility(isVisible = false)

            mainViewModel.rentals.observe(viewLifecycleOwner, rentalsObserver)
        }
    }


    private fun onRentalCancel() = { rental: Rental ->
    //    val cancelRentalDialog = CancelRentalDialog()
    //    fragmentManager.let { cancelRentalDialog.show(it!!, "Cancel dialog") }
        mainViewModel.isCancellationAllowed = true
        runBlocking {
            changeLoadingBarVisibility(true)
            withContext(Dispatchers.IO) { mainViewModel.cancelRental(rental.ID) }
            withContext(Dispatchers.IO) { mainViewModel.fetchRentals() }
            changeLoadingBarVisibility(false)
        }
    }

    private fun onRentalCall() = { rental: Rental ->
        /*
        TODO fetch centre for each rental and then enable phones
        val phone = rental.centre.phone
        val uri = Uri.parse("tel:$phone")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        if (intent.resolveActivity(activity!!.packageManager) != null) startActivity(intent)
        else toast("Cannot launch activity")
        */
    }

    private fun onRentalMap() = { rental: Rental ->
        /*
        TODO same as above
        val coordinateXFormatted = formatCoordinate(rental.centre.coordinateX, 4) // TODO !!.
        val coordinateYFormatted = formatCoordinate(rental.centre.coordinateY, 4)
        val label = rental.centre.name
        val uri = Uri.parse("geo:0,0?q=$coordinateXFormatted,$coordinateYFormatted($label)")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(activity!!.packageManager) != null) startActivity(intent)
        else toast("Cannot launch activity")
        */
    }


    override fun changeLoadingBarVisibility(isVisible: Boolean) {
        super.changeLoadingBarVisibility(isVisible)
        if (isVisible) {
            linearLayout_rentals_loading.visibility = View.VISIBLE
        } else {
            linearLayout_rentals_loading.visibility = View.GONE
        }
    }

    private val rentalsObserver = Observer<List<Rental>> {
        adapter.setRentals(ArrayList(it))
        // linearLayout_rentals_loading.visibility = View.GONE
        if (it.isNotEmpty()) {
            imageView_no_rentals.visibility = View.GONE
            textView_no_rentals.visibility = View.GONE
            recyclerView_rentals.visibility = View.VISIBLE
        } else {
            imageView_no_rentals.visibility = View.VISIBLE
            textView_no_rentals.visibility = View.VISIBLE
            recyclerView_rentals.visibility = View.GONE
        }
    }
}
