package com.pwr.sailapp.ui.main

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
import com.pwr.sailapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_rent_master.*
import java.lang.StringBuilder

// TODO consider using generic adapter for view model

class ProfileFragment : Fragment() {

    // TODO consider changing var to val
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
        val adapter = RentalAdapter(context!!) {rental:Rental -> Toast.makeText(requireContext(), "Starts ${rental.rentDate}", Toast.LENGTH_SHORT).show()}
        recyclerView_rentals.adapter = adapter

        /*
         Use ViewModelProviders to get the same instance and of(activity) to get context of activity - not a single fragment
         of(//activity) means: viewModel is scoped to activity lifecycle. When activity gets destroyed - viewModel too
        */
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        val navController = findNavController()
        // Observe liveData
        mainViewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState){
                MainViewModel.AuthenticationState.AUTHENTICATED -> showWelcomeMessage()
                else -> navController.navigate(R.id.destination_login)
            }
        })

        // TODO fix app crashing
        mainViewModel.rentals.observe(viewLifecycleOwner, Observer {
        //    val rentalsText = StringBuilder()
        //    for(rental in it) rentalsText.append(rental.toString(), "\n")
        //    textView_profile_rentals.text = rentalsText
            adapter.setRentals(it)

        } )
    }

    private fun showWelcomeMessage() = Toast.makeText(requireContext(), "Welcome to your profile!", Toast.LENGTH_SHORT).show()



}
