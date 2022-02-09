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
import drewcarlson.coingecko.models.coins.CoinFullData
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

    suspend fun getCoinInfoByID(coinID: String): CoinFullData? {
        return try {
            coinService.getCoinByID(coinID);
        } catch (e: Exception) {
            null
        }
    }

}