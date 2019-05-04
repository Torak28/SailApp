package com.pwr.sailapp.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.pwr.sailapp.R
import com.pwr.sailapp.data.Centre
import com.pwr.sailapp.ui.main.adapters.CentreAdapter
import com.pwr.sailapp.ui.main.dialogs.FilterDialogFragment
import com.pwr.sailapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_rent_master.*


class RentMasterFragment : Fragment(){

    companion object {
        const val MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rent_master, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_master.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context!!)
        // recyclerView_master.hasFixedSize() // makes recycler view more efficient
        val adapter =
            CentreAdapter(context!!) { centre: Centre -> centreItemClicked(centre) } // lambda as a second argument - can be moved out of brackets ()
        recyclerView_master.adapter = adapter

        // View model is scoped to lifecycle of the underling activity - it is kept in memory even if fragment is detached => share data between fragments!
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)

        // Pass this (fragment) as owner - updating is bound to states of fragment's lifecycle
        mainViewModel.centres.observe(viewLifecycleOwner, Observer {
            adapter.setCentres(it)
        })

        search_view.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                mainViewModel.search(newText)
                return false
            }
        })

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            Log.d("Access coarse location", "Permission denied")

            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                MY_PERMISSIONS_ACCESS_COARSE_LOCATION)
        }

        locateUser()

        button_location.setOnClickListener {locateUser()}

        button_filter.setOnClickListener {
            val filterDialog = FilterDialogFragment()
            filterDialog.setTargetFragment(this, 1) // fragment communication - TODO consider shared view model
            fragmentManager?.let { it1 -> filterDialog.show(it1, "Filter dialog") }
        }

        button_sort.setOnClickListener {  } // TODO implement sorting
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            MY_PERMISSIONS_ACCESS_COARSE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                }
            }
            // ... other permission codes if necessary
        }
    }

    private fun centreItemClicked(centre: Centre){
        Toast.makeText(requireContext(), "Clicked ${centre.name}", Toast.LENGTH_SHORT).show()
        mainViewModel.selectCentre(centre)
        findNavController().navigate(R.id.destination_rent_details) // TODO consider using nextAction and graph
    }

    private fun locateUser(){
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    mainViewModel.coordinates = Pair(location!!.latitude, location.longitude)

                    //        search_view.setQuery(mainViewModel.coordinates.toString(), false) // TODO use live data
                    // TODO calculate distance of centres
                    mainViewModel.calculateDistances(location)
                    mainViewModel.sort()
                }
                .addOnFailureListener{
                    Log.e("fusedLocationClient", "last location failure")
                    /*         if (it is ResolvableApiException){
                                 // Location settings are not satisfied, but this can be fixed
                                 // by showing the user a dialog.
                                 try {
                                     // Show the dialog by calling startResolutionForResult(),
                                     // and check the result in onActivityResult().
                                     it.startResolutionForResult(this@MainActivity,
                                         REQUEST_CHECK_SETTINGS)
                                 } catch (sendEx: IntentSender.SendIntentException) {
                                     // Ignore the error.
                                 }
                             }*/
                }
        } catch (e: SecurityException) {
            Log.e("SecurityException", "Missing location permission")
        }
    }



}
