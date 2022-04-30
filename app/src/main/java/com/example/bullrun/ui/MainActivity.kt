package com.example.bullrun.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bullrun.R
import com.example.bullrun.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        lifecycleScope.launch {
            viewModel.bottomNavigationState.collectLatest {
                if (it == "VISIBLE") {
                    window.navigationBarColor = ContextCompat.getColor(this@MainActivity,R.color.primary)
                    binding.bottomNavCard.visibility = View.VISIBLE
                } else if (it == "HIDDEN") {
                    window.navigationBarColor = ContextCompat.getColor(this@MainActivity,R.color.white)
                    binding.bottomNavCard.visibility = View.INVISIBLE
                }

            }
        }


        //since newer versions of navigation component, fragment states are saved while navigating with bottom navigation.
        //in this project I have used separate navigation graph for each fragment attached to bottom navigation,
        // so they handle their backstack on their own like the behaviour on instagram
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)


        navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener { navC, dest, arg ->

//            Log.d("TAGN","${binding.bottomNav.selectedItemId}}")
//            Log.d("TAGN","${dest.displayName}}")

            val stack = navController.backQueue.map { it.destination.displayName }
            Log.d("TAGN", "${stack}")
//            when {
//                stack.any { it.contains("home") } -> {
//                    binding.bottomNav.selectedItemId = R.id.homeFragment
//                    Log.d("TAGN", "home")
//                }
//                stack.any { it.contains("mainList") }-> {
//                    binding.bottomNav.selectedItemId = R.id.mainListFragment
//                    Log.d("TAGN", "mainList")
//                }
//                stack.any { it.contains("exchanges") } -> {
//                    binding.bottomNav.selectedItemId = R.id.exchangesFragment
//                    Log.d("TAGN", "exchanges")
//                }
//                else -> {
//                    binding.bottomNav.selectedItemId = R.id.portfolioFragment
//                }
//            }
        })


//        binding.bottomNav.setOnItemSelectedListener {
//            Log.d("TAGB","$it")
//            true
//        }


//        supportFragmentManager.beginTransaction()
//            .replace(R.id.nav_host_fragment, PortfolioFragment())
//            .commit()

//        binding.bottomNav.setOnItemSelectedListener {
//            binding.bottomNav.onItem
//            when (it.itemId) {
//                R.id.fragmentMainList -> {
////                    supportFragmentManager.beginTransaction()
////                        .replace(R.id.nav_host_fragment, MainListFragment())
////                        .addToBackStack(null)
////                        .commit()
//                    //navController.navigate
//                }
//                R.id.portfolioFragment -> {
//
//                }
//            }
//            Log.d("TAGB", "$it")
//            true
//        }

//        binding.bottomNav.setOnItemReselectedListener {
//            true
//        }
    }

}