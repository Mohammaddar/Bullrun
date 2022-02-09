package com.example.bullrun.ui.coinInfo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CoinInfoViewModelFactory(
    private val application: Application,
    private val coinId:String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinInfoViewModel::class.java)) {
            return CoinInfoViewModel(application,coinId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

