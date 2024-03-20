package com.msid.tabapay.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.msid.tabapay.R

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Initialize NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

// Setup AppBarConfiguration for top-level destinations
        appBarConfiguration = AppBarConfiguration(setOf(
            // specify the ids of the top-level destinations
            R.id.recipientListFragment,
            R.id.transaction_history_fragment,
            R.id.allTransactionHistoryFragment
            // Add other ids if they are also top-level destinations
        ))

        // Set up Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set up ActionBar with NavController and AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.blue))




        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))

        bottomNavigationView.setupWithNavController(navController)



    }

    override fun onSupportNavigateUp(): Boolean {
        // Handle navigation when the back button is tapped
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Method to navigate to PaymentAmountFragment from RecipientListFragment
    fun navigateToPaymentAmountFragment() {
        navController.navigate(R.id.action_recipientListFragment_to_paymentAmountFragment)
    }

    // Method to navigate to PaymentProcessFragment from PaymentAmountFragment
    fun navigateToPaymentProcessFragment() {
        navController.navigate(R.id.action_paymentAmountFragment_to_recipientListFragment)
    }

    // Method to navigate to RecipientListFragment from PaymentProcessFragment
    fun navigateToRecipientListFragment() {
        navController.navigate(R.id.action_paymentProcessFragment_to_recipientListFragment)
    }

    // Method to navigate to TransactionHistoryFragment from RecipientListFragment
    fun navigateToTransactionHistoryFragment() {
        navController.navigate(R.id.action_recipientListFragment_to_transactionHistoryFragment)

    }
}