package com.example.bullrun.ui.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bullrun.data.repos.coin.CoinRepository
import com.example.bullrun.ui.model.GlobalDataUI
import com.example.bullrun.ui.model.TopMoverUi
import com.example.bullrun.ui.model.TrendingCoin
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val coinRepository = CoinRepository.getRepository(application)
    val topTenCoinsList = liveData {
        val ls = coinRepository.getTopTenCoins()
        emit(ls)
    }

    val topMovers = MutableLiveData<HashMap<String, List<TopMoverUi>>>()

    val trending = MutableLiveData<List<TrendingCoin>>()
    //data for top coin chart(BTC)
    val topCoinPrices1D=MutableLiveData<List<List<String>>>()

    val globalData=MutableLiveData<GlobalDataUI>()


    init {
        getTopMovers()
        getTrending()
        getTopCoinMarketChart()
        getGlobal()
    }

    private fun getTopMovers() {
        viewModelScope.launch {
            topMovers.value = coinRepository.getTopMovers()
        }
    }

    private fun getTrending() {
        viewModelScope.launch {
            trending.value = coinRepository.getTrendings()
        }
    }

    private fun getTopCoinMarketChart(){
        viewModelScope.launch {
            topCoinPrices1D.value=coinRepository.getTopCoinChart().prices
        }
    }

    private fun getGlobal(){
        viewModelScope.launch {
            globalData.value=coinRepository.getGlobalData()
        }
    }
}