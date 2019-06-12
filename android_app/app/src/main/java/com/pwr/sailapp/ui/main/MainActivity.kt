package com.pwr.sailapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.pwr.sailapp.R
import com.pwr.sailapp.data.sail.AuthenticationState
import com.pwr.sailapp.data.sail.User
import com.pwr.sailapp.internal.NetworkStatus
import com.pwr.sailapp.ui.generic.ScopedActivity
import com.pwr.sailapp.viewModel.MainViewModel
import com.pwr.sailapp.viewModel.getViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigationview_header.view.*
import kotlinx.coroutines.*

const val DISCONNECTED_MSG = "No connection"

class MainActivity : ScopedActivity() {

    // Navigating to a destination is done using a NavController,
    // an object that manages app navigation within a NavHost.
    // Each NavHost has its own corresponding NavController
    private lateinit var navController: NavController

    private val mainViewModel by lazy {
        getViewModel { MainViewModel(application) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(my_toolbar)


        navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            item.isChecked = true // menuItem is now highlighted
            drawerLayout.closeDrawers()
            true
        }
        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment)
        navigationView.setupWithNavController(navController)


        mainViewModel.authenticationState.observe(this, Observer {
            when (it) {
                null -> Log.e("MainActivity", "mainViewModel.authenticationState = null")
                AuthenticationState.UNAUTHENTICATED -> findNavController(R.id.my_nav_host_fragment).navigate(R.id.destination_login)
                else -> return@Observer
            }
        })


        val logoutItem = navigationView.menu.findItem(R.id.destination_logout)

        logoutItem.setOnMenuItemClickListener {
            Toast.makeText(applicationContext, "Logging out", Toast.LENGTH_SHORT).show()
            mainViewModel.logOut()
            drawerLayout.closeDrawers()
            true
        }

        mainViewModel.networkStatus.observe(this, networkStatusObserver)

        launch {
            val operation = async {
                mainViewModel.fetchUser()
            }
            operation.await()

            mainViewModel.user.observe(this@MainActivity, Observer {
                if (it == null) {
                    Log.e("MainAct", "user = null"); return@Observer
                }
                changeNavigationHeaderInfo(it)
            })

        }
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

    // Changing the navigation header info
    private fun changeNavigationHeaderInfo(user: User) {
        val headerView = navigationView.getHeaderView(0)
        headerView.textView_user_nav_name.text = user.firstName
    }

    private val networkStatusObserver = Observer<NetworkStatus> {
        when (it) {
            NetworkStatus.DISCONNECTED -> toast(DISCONNECTED_MSG)
            else -> {}
        }
    }
}
