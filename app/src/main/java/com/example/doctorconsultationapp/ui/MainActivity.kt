package com.example.doctorconsultationapp.ui

import android.app.ProgressDialog.show
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.doctorconsultationapp.R
import com.example.doctorconsultationapp.sharedPreferences.LoginPreferences
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var session: LoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController: NavController = findNavController(R.id.fragmentContainerView)

        // find the navigation view by its id
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        // get the header view of the navigation view
        val headerView = navigationView.getHeaderView(0)

        // find the TextView by its id inside the header view
        val userName: TextView = headerView.findViewById(R.id.user_name)
        var userEmail: TextView = headerView.findViewById(R.id.user_email)

        // update the textview to the current logged in user
        userName.text = intent.getStringExtra("userName")
        userEmail.text = intent.getStringExtra("userEmail")

        session = LoginPreferences(this)

        // Set up the action bar toggle
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//      # This section of code is to link the nav buttons to their respective fragment
        navView.menu.findItem(R.id.nav_profile).setOnMenuItemClickListener {
            // TODO: Go to ProfileFragment
            navController.navigate(R.id.profileFragment)
            // to close the drawer (navigationView)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        navView.menu.findItem(R.id.nav_doctor).setOnMenuItemClickListener {
            navController.navigate(R.id.doctorListFragment)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        navView.menu.findItem(R.id.nav_appointment).setOnMenuItemClickListener {
            navController.navigate(R.id.appointmentHistoryFragment)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            Log.d("Testing", "${session.getUserDetails()}")
            session.LogoutUser()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}