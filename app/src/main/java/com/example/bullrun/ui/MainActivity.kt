package com.example.bullrun.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bullrun.R
import com.example.bullrun.databinding.ActivityMainBinding
import com.example.bullrun.ui.fragments.mainList.MainListFragment
import com.example.bullrun.ui.fragments.portfolio.PortfolioFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)

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

        binding.bottomNav.setOnItemReselectedListener {
            true
        }
    }

}