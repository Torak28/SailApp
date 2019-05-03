package com.pwr.sailapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.pwr.sailapp.R
import com.pwr.sailapp.data.Centre
import com.pwr.sailapp.ui.main.adapters.CentreAdapter
import com.pwr.sailapp.ui.main.dialogs.FilterDialogFragment
import com.pwr.sailapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_rent_master.*


class RentMasterFragment : Fragment(){

    private lateinit var mainViewModel: MainViewModel

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

        button_filter.setOnClickListener {
            val filterDialog = FilterDialogFragment()
            filterDialog.setTargetFragment(this, 1) // fragment communication - TODO consider shared view model
            fragmentManager?.let { it1 -> filterDialog.show(it1, "Filter dialog") }
        }

        button_sort.setOnClickListener {  }
    }

    private fun centreItemClicked(centre: Centre){
        Toast.makeText(requireContext(), "Clicked ${centre.name}", Toast.LENGTH_SHORT).show()
        mainViewModel.selectCentre(centre)
        findNavController().navigate(R.id.destination_rent_details) // TODO consider using nextAction and graph
    }



}
