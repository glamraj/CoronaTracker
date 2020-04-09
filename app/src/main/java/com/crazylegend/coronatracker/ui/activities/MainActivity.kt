package com.crazylegend.coronatracker.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.crazylegend.coronatracker.R
import com.crazylegend.coronatracker.abstracts.AbstractActivity
import com.crazylegend.coronatracker.databinding.ActivityMainBinding
import com.crazylegend.coronatracker.utils.setupWithNavController
import com.crazylegend.coronatracker.vms.MainActivityViewModel
import com.crazylegend.kotlinextensions.livedata.compatProvider
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible

class MainActivity : AbstractActivity() {

    override val binding by viewBinding(ActivityMainBinding::inflate)
    private lateinit var mainActivityViewModel: MainActivityViewModel


    private var currentNavController: LiveData<NavController>? = null


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel = compatProvider()
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

    }
    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {

        val navGraphIds = listOf(R.navigation.countries, R.navigation.statistics)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = binding.bottomNavigation.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.navHostContainer,
                intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
        controller.value?.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailedCountryFragment -> {
                    hideBottomBar()
                }
                else -> {
                    showBottomBar()
                }
            }
        }
    }

    private fun hideBottomBar() {
        binding.bottomNavigation.gone()
    }

    private fun showBottomBar() {
        binding.bottomNavigation.visible()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }


    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

}
