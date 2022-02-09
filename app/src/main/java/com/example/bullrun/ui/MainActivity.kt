package com.example.bullrun.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bullrun.R
import com.example.bullrun.databinding.ActivityMainBinding
import com.example.bullrun.ui.mainList.MainListFragment
import com.example.bullrun.ui.portfolio.PortfolioFragment
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


//        var transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.nav_host_fragment, PortfolioFragment())
//        transaction.commit()
//
//        binding.bottomNav.setOnItemSelectedListener{
//            when (it.itemId) {
//                R.id.fragmentMainList -> {
//                    val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//                    fragmentTransaction.replace(R.id.content, MainListFragment())
//                    fragmentTransaction.addToBackStack(null)
//                    fragmentTransaction.commit()
//                    return true
//                }
//                R.id.navigation_profile -> {
//                    selectedFragment = ItemtwoFragment.newInstance()
//                    transaction = supportFragmentManager.beginTransaction()
//                    transaction.replace(R.id.content, selectedFragment)
//                    transaction.addToBackStack(null)
//                    transaction.commit()
//                    return true
//                }
//            }
//            true
//        }
    }

}