package com.example.bullrun.ui.coinInfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bullrun.data.repos.coin.CoinRepository
import com.example.bullrun.data.database.model.Coin
import drewcarlson.coingecko.models.coins.CoinFullData
import kotlinx.coroutines.launch

class CoinInfoViewModel(application: Application, val coinId: String) : AndroidViewModel(application) {

    private val coinRepository = CoinRepository.getRepository(application)

    val coinInfo = MutableLiveData<CoinFullData>()

    init {
        getFullCoinInfo()
    }

    private fun getFullCoinInfo(){
        viewModelScope.launch {
            coinInfo.value = coinRepository.getCoinInfoByID(coinId)
        }
    }

    fun getSmallCoinInfo():Coin?{
        val info=coinInfo.value;
        return info?.let {
            Coin(
                coinId = it.id,
                marketCapRank = it.marketCapRank,
                coinName = it.name,
                priceChangePercentage24h = it.marketData?.priceChangePercentage24h ?: 0.0,
                currentPrice = it.marketData?.currentPrice?.get("usd") ?: 0.0,
                image = it.image.large
            )
        }
    }
}