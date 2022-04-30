package com.example.bullrun.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bullrun.data.repos.coin.CoinRepository
import com.example.bullrun.data.database.model.Coin
import drewcarlson.coingecko.models.coins.CoinFullData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    val bottomNavigationState= MutableStateFlow("VISIBLE")

}