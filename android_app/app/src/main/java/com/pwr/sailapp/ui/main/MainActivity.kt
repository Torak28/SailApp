package com.pwr.sailapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.pwr.sailapp.R
import com.pwr.sailapp.data.AuthenticationState
import com.pwr.sailapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigationview_header.view.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    // Navigating to a destination is done using a NavController,
    // an object that manages app navigation within a NavHost.
    // Each NavHost has its own corresponding NavController
    private lateinit var navController: NavController
    private lateinit var mainViewModel: MainViewModel

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        setContentView(R.layout.activity_main)
        setSupportActionBar(my_toolbar)

        navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            item.isChecked = true // menuItem is now highlighted
            drawerLayout.closeDrawers()
            true
        }

        // Handling navigation ...
        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment)
        // Add nav controller to drawer's toolbar
        navigationView.setupWithNavController(navController)

        // Use ViewModelProvider to get the same instance of viewModel - not to instantiate new viewModel each  time
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        mainViewModel.authenticationState.observe(this, Observer {
            when(it){
                null -> Log.e("MainActivity", "mainViewModel.authenticationState = null")
                AuthenticationState.UNAUTHENTICATED -> findNavController(R.id.my_nav_host_fragment).navigate(R.id.destination_login)
                else -> return@Observer
            }
        })


        val logoutItem = navigationView.menu.findItem(R.id.destination_logout)

        logoutItem.setOnMenuItemClickListener {
            Toast.makeText(applicationContext, "Logging out", Toast.LENGTH_SHORT).show()
            launch {
                // show loading bar Logging out ...
                val logoutOperation = async {
                    mainViewModel.logOut()
                }
                logoutOperation.await()
                // hide loading bar ...
                drawerLayout.closeDrawers()
            }
            true
        }
       //  changeNavigationHeaderInfo() // set user name in navigation header
    }


    // Opening the drawer menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    // Changing the navigation header info
    private fun changeNavigationHeaderInfo() {
        val headerView = navigationView.getHeaderView(0)
        headerView.textView_user_nav_name.text = mainViewModel.currentUser.firstName
    }
}
