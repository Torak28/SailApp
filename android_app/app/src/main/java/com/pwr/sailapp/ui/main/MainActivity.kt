package com.pwr.sailapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.pwr.sailapp.R
import com.pwr.sailapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // Navigating to a destination is done using a NavController,
    // an object that manages app navigation within a NavHost.
    // Each NavHost has its own corresponding NavController
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(my_toolbar)


        // TODO use data binding
        navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            item.isChecked = true // menuItem is now highlighted
            drawerLayout.closeDrawers()
            true
        }

        // Handling navigation ...
        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment)
        // Add nav controller to drawer's toolbar
        navigationView.setupWithNavController(navController)

        // TODO find better way to logout
        // Use ViewModelProvider to get the same instance of viewModel - not to instantiate new viewModel each  time
        val mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val logoutItem = navigationView.menu.findItem(R.id.destination_logout)
        logoutItem.setOnMenuItemClickListener {
            Toast.makeText(applicationContext, "Logging out", Toast.LENGTH_SHORT).show()
            mainViewModel.logOut() // removing user data from shared preferences
            drawerLayout.closeDrawers()
            findNavController(R.id.my_nav_host_fragment).navigate(R.id.destination_profile)
            true
        }
    }


    // Opening the drawer menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else  -> super.onOptionsItemSelected(item)
        }
    }
}
