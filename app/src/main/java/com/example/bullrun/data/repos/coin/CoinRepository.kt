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
import com.example.bullrun.ui.model.GlobalDataUI
import com.example.bullrun.ui.model.TopMoverUi
import com.example.bullrun.ui.model.TrendingCoin
import drewcarlson.coingecko.models.coins.CoinFullData
import drewcarlson.coingecko.models.coins.CoinMarkets
import drewcarlson.coingecko.models.coins.MarketChart
import kotlinx.coroutines.flow.Flow

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
        try {
            coinDataBase.CoinListDao.insertAll(coinService.getAllCoinsList().map {
                com.example.bullrun.data.database.model.CoinList(
                    coinId = it.id,
                    coinName = it.name,
                    coinSymbol = it.symbol
                )
            })
        } catch (e: Exception) {
            Log.d("TAG", e.message.toString())
        }
    }

    suspend fun isCoinsListAlreadyLoaded(): Boolean {
        return coinDataBase.CoinListDao.isTableEmpty() == 0
    }

    suspend fun getAllCoinsList(query: String): List<com.example.bullrun.data.database.model.CoinList> {
        return coinDataBase.CoinListDao.getAll("%${query.replace(' ', '%')}%");
    }

    suspend fun getCoinInfoByID(coinId: String,tickers: Boolean,communityData: Boolean,developerData: Boolean,sparkline: Boolean): CoinFullData? {
        return try {
            coinService.getCoinByID(coinId,tickers,communityData,developerData,sparkline);
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getTopTenCoins(): List<CoinMarkets> {
        return try {
            coinService.getCoins(query = "", page = 1, pageSize = 10)
        } catch (e: Exception) {
            Log.d("TAG", e.message.toString())
            listOf<CoinMarkets>()
        }
    }

    suspend fun getChartByID(coinID: String,days:Double): MarketChart? {
        return try {
            coinService.getMarketChart(coinID,days);
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getTopMovers(): HashMap<String, List<TopMoverUi>> {
        val ls = coinService.getTop250Coins().sortedByDescending { it.priceChangePercentage24h }
        return hashMapOf(
            "Gainers" to ls.subList(0, 10)
                .map {
                    TopMoverUi(
                        it.id,
                        it.name,
                        it.symbol,
                        it.priceChange24h,
                        it.image,
                        it.currentPrice
                    )
                },
            "Losers" to ls.subList(ls.size - 10, ls.size)
                .map {
                    TopMoverUi(
                        it.id,
                        it.name,
                        it.symbol,
                        it.priceChange24h,
                        it.image,
                        it.currentPrice
                    )
                }
        )
    }

    suspend fun getTrendings():List<TrendingCoin> {
        return coinService.getTrendingCoins().map { it.item.run { TrendingCoin(id,name,symbol,score,small,priceBtc) } }
    }

    suspend fun getTopCoinChart(): MarketChart {
        val ls=coinService.getTopCoinMarketChart()
        Log.d("TAGDATA","$ls")
        return ls
    }

    suspend fun getGlobalData(): GlobalDataUI {
        return GlobalDataUI.transformDataModelToUiModel(coinService.getGlobalData())
    }

}