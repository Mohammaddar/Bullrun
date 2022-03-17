package com.example.bullrun.data.repos.portfolio

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.room.withTransaction
import com.example.bullrun.App
import com.example.bullrun.data.database.CoinDatabase
import com.example.bullrun.data.database.model.Asset
import com.example.bullrun.data.database.model.Transaction
import com.example.bullrun.data.database.model.Wallet
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


    suspend fun buyAsset(
        coinId: String,
        coinName: String,
        image: String,
        symbol: String,
        currentPrice: Double,
        price: Double,
        volume: Double,
        walletName: String
    ) {
        val portfolioDao = coinDataBase.portfolioDao
        coinDataBase.withTransaction {
            try {
                if (portfolioDao.isAssetExisted(coinId, walletName) == 1) {
                    portfolioDao.buyAsset(coinId, volume, volume * price, walletName)
                } else {
                    portfolioDao.addAsset(
                        Asset(
                            coinId = coinId,
                            coinName = coinName,
                            image = image,
                            symbol = symbol,
                            currentPrice = currentPrice,
                            totalBuyingVolume = volume,
                            totalSellingVolume = 0.0,
                            totalBuyingCost = price * volume,
                            totalSellingIncome = 0.0,
                            walletName = walletName
                        )
                    )
                }
                coinDataBase.transactionDao.addTransaction(
                    Transaction(
                        type = "Buy",
                        coinId = coinId,
                        volume = volume,
                        price = price,
                        walletName = walletName,
                        dateMillis = System.currentTimeMillis()
                    )
                )
            } catch (e: java.lang.Exception) {
                Toast.makeText(
                    App.applicationContext(),
                    "Error occured message is ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    suspend fun sellAsset(coinId: String, volume: Double, price: Double, walletName: String) {
        coinDataBase.portfolioDao.sellAsset(coinId, volume, volume * price, walletName)

        coinDataBase.transactionDao.addTransaction(
            Transaction(
                type = "Sell",
                coinId = coinId,
                volume = volume,
                price = price,
                walletName = walletName,
                dateMillis = System.currentTimeMillis()
            )
        )
    }

    fun getAllAssetsInWallet(walletName: String): Flow<List<Asset>> {
        return coinDataBase.portfolioDao.getAllAssetsInWallet(walletName)
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

    suspend fun addNewWallet(wallet: Wallet) {
        coinDataBase.walletDao.addWallet(wallet)
    }

    fun getAllWallets(): Flow<List<Wallet>> {
        return coinDataBase.walletDao.getAll()
    }

    fun tryOrCatch(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            Toast.makeText(
                App.applicationContext(),
                "Error occured message is ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}