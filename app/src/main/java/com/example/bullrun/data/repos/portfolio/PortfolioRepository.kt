package com.example.bullrun.data.repos.portfolio

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.room.withTransaction
import com.example.bullrun.App
import com.example.bullrun.data.database.CoinDatabase
import com.example.bullrun.data.database.model.Asset
import com.example.bullrun.data.remote.CoinService
import kotlinx.coroutines.flow.Flow

class PortfolioRepository private constructor(context: Context) {

    companion object {
        @Volatile
        private var repo: PortfolioRepository? = null
        fun getRepository(context: Context): PortfolioRepository {
            synchronized(this) {
                return repo ?: PortfolioRepository(context)
            }
        }

    }

    private val coinDataBase = CoinDatabase.getInstance(context)
    private val coinService = CoinService.getInstance()

    suspend fun getAssetById(coinId: String): Asset? {
        return coinDataBase.portfolioDao.getAssetById(coinId)
    }

    suspend fun buyAsset(
        coinId: String,
        coinName: String,
        image: String,
        symbol: String,
        currentPrice: Double,
        price: Double,
        volume: Double
    ) {
        val dao = coinDataBase.portfolioDao
        if (dao.isAssetExisted(coinId) == 1) {
            dao.buyAsset(coinId, volume, volume * price)
        } else {
            dao.addAsset(
                Asset(
                    coinId = coinId,
                    coinName = coinName,
                    image = image,
                    symbol = symbol,
                    currentPrice = currentPrice,
                    totalBuyingVolume = volume,
                    totalSellingVolume = 0.0,
                    totalBuyingCost = price * volume,
                    totalSellingIncome = 0.0
                )
            )
        }
    }

    suspend fun sellAsset(coinId: String, volume: Double, price: Double) {
        coinDataBase.portfolioDao.sellAsset(coinId, volume, volume * price)
    }

    fun getAllAssets(): Flow<List<Asset>> {
        return coinDataBase.portfolioDao.getAll()
    }

    suspend fun updateAssetsInfo(ids: List<String>) {
        try {
            val assets = coinService.getCoinsByIDs(ids)
            coinDataBase.withTransaction {
                Log.d("TAGP", "thread10: ${Thread.currentThread().name}")
                assets.forEach {
                    it.id?.let { it1 ->
                        coinDataBase.portfolioDao.updateCurrentPrice(
                            it1,
                            it.currentPrice
                        )
                    }
                }
            }

        } catch (e: Exception) {
            Toast.makeText(
                App.applicationContext(),
                "Error occured message is ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}