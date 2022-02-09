package com.example.bullrun.ui.transaction

import android.app.Application
import androidx.lifecycle.*
import com.example.bullrun.data.database.model.Asset
import com.example.bullrun.data.repos.coin.CoinRepository
import com.example.bullrun.data.repos.portfolio.PortfolioRepository
import drewcarlson.coingecko.models.coins.CoinFullData
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    val context = application

    private val coinRepository = CoinRepository.getRepository(application)
    private val portfolioRepository = PortfolioRepository.getRepository(application)

    var coinId = MutableLiveData<String>()


    var coin = coinId.switchMap {
        liveData {
            coinRepository.getCoinInfoByID(it)?.let { it1 -> emit(it1) }
        }
    }


    var asset = coinId.switchMap {
        liveData<Asset> {
            portfolioRepository.getAssetById(it)
        }
    }


    fun buyAsset(buyingPrice: Double, buyingVolume: Double) {
        viewModelScope.launch {
            coin.value?.let {
                portfolioRepository.buyAsset(
                    coinId = it.id,
                    coinName = it.name,
                    image = it.image.large ?: "",
                    symbol = it.symbol,
                    currentPrice = it.marketData?.currentPrice?.get("usd") ?: 0.0,
                    price = buyingPrice,
                    volume = buyingVolume
                )
            }
        }
    }

    fun sellAsset(sellingPrice: Double, sellingVolume: Double) {
        viewModelScope.launch {
            coin.value?.let {
                portfolioRepository.sellAsset(
                    coinId = it.id,
                    volume = sellingVolume,
                    price = sellingPrice
                )
            }
        }
    }
}


