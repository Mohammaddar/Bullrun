package com.example.bullrun.ui.fragments.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.bullrun.data.repos.coin.CoinRepository
import com.example.bullrun.data.database.model.Coin
import drewcarlson.coingecko.models.coins.CoinMarkets
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val coinRepository = CoinRepository.getRepository(application)
    val topTenCoinsList = liveData {
        Log.d("TAGD","start")
        val ls=coinRepository.getTopTenCoins()
        emit(ls)
        Log.d("TAGD","end")
    }

}