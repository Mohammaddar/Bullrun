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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val coinRepository = CoinRepository.getRepository(application)
    val topTenCoinsList = liveData {
        val ls = coinRepository.getTopTenCoins()
        ls?.let {
            emit(ls)
        }
    }

    val topMovers = MutableLiveData<HashMap<String, List<TopMoverUi>>>()

    val trending = MutableLiveData<List<TrendingCoin>?>()

    //data for top coin chart(BTC)
    val topCoinPrices1D = MutableLiveData<List<List<String>>?>()

    val globalData = MutableLiveData<GlobalDataUI?>()


    init {
        getTopMovers()
        getTrending()
        getTopCoinMarketChart()
        getGlobal()
    }

    private fun getTopMovers() {
        viewModelScope.launch() {
            val top250Coins =
                coinRepository.getTop250Coins()?.sortedByDescending { it.priceChangePercentage24h }
            top250Coins?.let {
                val job = async(Dispatchers.Default) {
                    hashMapOf(
                        "Gainers" to top250Coins.subList(0, 10)
                            .map {
                                TopMoverUi(
                                    it.id,
                                    it.name,
                                    it.symbol,
                                    it.priceChangePercentage24h,
                                    it.image,
                                    it.currentPrice
                                )
                            },
                        "Losers" to top250Coins.subList(top250Coins.size - 10, top250Coins.size)
                            .map {
                                TopMoverUi(
                                    it.id,
                                    it.name,
                                    it.symbol,
                                    it.priceChangePercentage24h,
                                    it.image,
                                    it.currentPrice
                                )
                            }
                    )
                }
                topMovers.value = job.await()
            }
        }
    }

    private fun getTrending() {
        viewModelScope.launch {
            trending.value = coinRepository.getTrending()
        }
    }

    private fun getTopCoinMarketChart() {
        viewModelScope.launch {
            topCoinPrices1D.value = coinRepository.getTopCoinChart()?.prices
        }
    }

    private fun getGlobal() {
        viewModelScope.launch() {
            val gd = coinRepository.getGlobalData()
            gd?.let {
                val job = async(Dispatchers.Default) {
                    GlobalDataUI.transformDataModelToUiModel(it)
                }
                globalData.value = job.await()
            }
        }
    }
}