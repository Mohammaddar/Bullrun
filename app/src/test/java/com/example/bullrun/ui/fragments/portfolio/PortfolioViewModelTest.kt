package com.example.bullrun.ui.fragments.portfolio

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bullrun.getOrAwaitValue
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PortfolioViewModelTest {

    // Subject under test
    private lateinit var tasksViewModel: PortfolioViewModel

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    //Given
    @Before
    fun setupViewModel() {
        tasksViewModel = PortfolioViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    suspend fun addNewWallet_WalletAddedToWalletsList() {
        //When
        tasksViewModel.addWallet("testWallet")
        //Then
        assertThat(tasksViewModel.walletsList.getOrAwaitValue().map { it.name }
            .contains("testWallet"), CoreMatchers.`is`(true))
    }
}