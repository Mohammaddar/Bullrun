package com.example.bullrun.data.repos.coin

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.bullrun.data.database.CoinDatabase
import com.example.bullrun.data.database.model.Coin
import com.example.bullrun.data.remote.CoinService
import com.example.bullrun.data.repos.invokeOrCatch
import com.example.bullrun.ui.model.TrendingCoin
import drewcarlson.coingecko.models.coins.CoinFullData
import drewcarlson.coingecko.models.coins.CoinMarkets
import drewcarlson.coingecko.models.coins.MarketChart
import drewcarlson.coingecko.models.global.GlobalData
import kotlinx.coroutines.flow.Flow

//kinda refactored in 3/6/2022
class CoinRepository private constructor(context: Context) {

    companion object {
        @Volatile
        private var repo: CoinRepository? = null
        fun getRepository(context: Context): CoinRepository {
            synchronized(this) {
                return repo ?: CoinRepository(context)
            }
        }

    }

    private val coinDataBase = CoinDatabase.getInstance(context)
    private val coinService = CoinService.getInstance()

    fun getCoinsStream(query: String): Flow<PagingData<Coin>> {
        Log.d("GithubRepository", "New query: $query")
        Log.d("TAGT", "getCoinsStream : ${Thread.currentThread().name}")
        // appending '%' so we can allow other characters to be before and after the query string
        val dbQuery = "%$query%"
        val pagingSourceFactory = { coinDataBase.coinDatabaseDao.getAll(dbQuery) }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 50, enablePlaceholders = false),
            remoteMediator = CoinsRemoteMediator(
                query,
                coinService,
                coinDataBase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun getAllCoinsListAndInsertToDB() {
        invokeOrCatch {
            coinDataBase.CoinListDao.insertAll(coinService.getAllCoinsList().map {
                com.example.bullrun.data.database.model.CoinList(
                    coinId = it.id,
                    coinName = it.name,
                    coinSymbol = it.symbol
                )
            })
        }
    }

    suspend fun isCoinsListAlreadyLoaded(): Boolean {
        return invokeOrCatch {
            coinDataBase.CoinListDao.isTableEmpty() == 0
        } ?: false
    }

    suspend fun getAllCoinsList(query: String): List<com.example.bullrun.data.database.model.CoinList> {
        return invokeOrCatch {
            coinDataBase.CoinListDao.getAll("%${query.replace(' ', '%')}%")
        } ?: listOf()
    }

    suspend fun getCoinInfoByID(
        coinId: String,
        tickers: Boolean,
        communityData: Boolean,
        developerData: Boolean,
        sparkline: Boolean
    ): CoinFullData? {
        return invokeOrCatch {
            coinService.getCoinByID(coinId, tickers, communityData, developerData, sparkline)
        }
    }

    suspend fun getTopTenCoins(): List<CoinMarkets>? {
        return invokeOrCatch {
            coinService.getCoins(query = "", page = 1, pageSize = 10)
        }
    }

    suspend fun getChartByID(coinID: String, days: Double): MarketChart? {
        return invokeOrCatch {
            coinService.getMarketChart(coinID, days)
        }
    }

    suspend fun getTop250Coins(): List<CoinMarkets>? {
        return invokeOrCatch {
            coinService.getTop250Coins()
        }
    }

    suspend fun getTrending(): List<TrendingCoin>? {
        return invokeOrCatch {
            coinService.getTrendingCoins()
                .map { it.item.run { TrendingCoin(id, name, symbol, score, small, priceBtc) } }
        }
    }

    suspend fun getTopCoinChart(): MarketChart? {
        return invokeOrCatch {
            coinService.getTopCoinMarketChart()
        }
    }

    suspend fun getGlobalData(): GlobalData? {
        return invokeOrCatch {
            coinService.getGlobalData()
        }
    }

    suspend fun getListOfCoinsById(ids:List<String>):List<CoinMarkets>?{
        return invokeOrCatch {
            coinService.getCoinsByIDs(ids)
        }
    }

}