package com.example.bullrun.data.repos.portfolio

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import com.example.bullrun.data.database.CoinDatabase
import com.example.bullrun.data.database.model.Asset
import com.example.bullrun.data.database.model.Transaction
import com.example.bullrun.data.database.model.TransactionType
import com.example.bullrun.data.database.model.Wallet
import com.example.bullrun.data.remote.CoinService
import com.example.bullrun.data.repos.invokeOrCatch
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
        priceChangePercentage24H: Double,
        price: Double,
        volume: Double,
        walletName: String,
    ) {
        invokeOrCatch {
            val portfolioDao = coinDataBase.portfolioDao
            coinDataBase.withTransaction {
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
                            priceChangePercentage24H=priceChangePercentage24H,
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
                        type = TransactionType.Buy,
                        coinId = coinId,
                        coinName = coinName,
                        symbol = symbol,
                        volume = volume,
                        price = price,
                        walletName = walletName,
                        dateMillis = System.currentTimeMillis(),
                        image = image
                    )
                )
            }
        }
    }

    suspend fun sellAsset(
        coinId: String,
        coinName: String,
        symbol: String,
        volume: Double,
        price: Double,
        walletName: String,
        image: String
    ) {
        invokeOrCatch {
            coinDataBase.portfolioDao.sellAsset(coinId, volume, volume * price, walletName)
            coinDataBase.transactionDao.addTransaction(
                Transaction(
                    type = TransactionType.Sell,
                    coinId = coinId,
                    coinName = coinName,
                    symbol = symbol,
                    volume = volume,
                    price = price,
                    walletName = walletName,
                    dateMillis = System.currentTimeMillis(),
                    image = image
                )
            )
        }
    }

    fun getAllAssetsInWalletFlow(walletName: String): Flow<List<Asset>> {
        return coinDataBase.portfolioDao.getAllAssetsInWalletFlow(walletName)
    }


    suspend fun getAllAssetsIDsInWallet(walletName: String): List<String> {
        return coinDataBase.portfolioDao.getAllAssetsIDsInWallet(walletName)
    }

    suspend fun updateAssetsInfoById(ids: List<String>) {
        invokeOrCatch {
            val assets = coinService.getCoinsByIDs(ids)
            coinDataBase.withTransaction {
                Log.d("TAGP", "updateAssetsInfo: ${Thread.currentThread().name}")
                assets.forEach {
                    it.id.let { it1 ->
                        coinDataBase.portfolioDao.updatePriceInfo(
                            it1,
                            it.currentPrice,
                            it.priceChangePercentage24h
                        )
                    }
                }
            }
            Log.d("TAGP", "updateAssetsInfo finish: ${Thread.currentThread().name}")
        }
    }

    suspend fun updateAllAssetsInfo() {
        invokeOrCatch {
            val ids=coinDataBase.portfolioDao.getAllAssetsIds()
            val assets = coinService.getCoinsByIDs(ids)
            coinDataBase.withTransaction {
                Log.d("TAGP", "updateAssetsInfo: ${Thread.currentThread().name}")
                assets.forEach {
                    it.id.let { it1 ->
                        coinDataBase.portfolioDao.updatePriceInfo(
                            it1,
                            it.currentPrice,
                            it.priceChangePercentage24h
                        )
                    }
                }
            }
            Log.d("TAGP", "updateAssetsInfo finish: ${Thread.currentThread().name}")
        }
    }

    suspend fun addNewWallet(wallet: Wallet) {
        invokeOrCatch { coinDataBase.walletDao.addWallet(wallet) }
    }

    fun getAllWalletsNamesFlow(): Flow<List<String>> {
        return coinDataBase.walletDao.getAllNamesFlow()
    }

    fun getTransactionsByWalletFlow(walletName: String): Flow<List<Transaction>> {
        return coinDataBase.transactionDao.getTransactionByWallet(walletName)
    }

    suspend fun getTransactionsByAssetAndWallet(
        assetName: String,
        walletName: String
    ): List<Transaction>? {
        return invokeOrCatch {
            coinDataBase.transactionDao.getTransactionByAssetAndWallet(
                assetName,
                walletName
            )
        }
    }

    fun getAllHoldingAssetsFlow():Flow<List<Asset>>{
        return coinDataBase.portfolioDao.getAllAssetsFlow()
    }

}