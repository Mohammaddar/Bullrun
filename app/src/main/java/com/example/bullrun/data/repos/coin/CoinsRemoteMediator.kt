package com.example.bullrun.data.repos.coin

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.bullrun.data.database.CoinDatabase
import com.example.bullrun.data.database.model.Coin
import com.example.bullrun.data.database.model.RemoteKeys
import com.example.bullrun.data.remote.CoinService
import drewcarlson.coingecko.error.CoinGeckoApiException
import java.util.*
import kotlin.coroutines.cancellation.CancellationException

private const val GITHUB_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class CoinsRemoteMediator(
    private val query: String,
    private val coinService: CoinService,
    private val coinDatabase: CoinDatabase
) : RemoteMediator<Int, Coin>() {

    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Coin>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: GITHUB_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        when (loadType) {
            LoadType.REFRESH -> {
                Log.d("TAG", "refresh $page")
            }
            LoadType.PREPEND -> {
                Log.d("TAG", "prepend $page")
            }
            LoadType.APPEND -> {
                Log.d("TAG", "append $page")
            }
        }
        //val apiQuery = query + IN_QUALIFIER
        Log.d("TAGT", "load1 : ${Thread.currentThread().name}")
        try {
            var coins = coinService.getCoins(query, page, state.config.pageSize)
            Log.d("TAG", "${coins.count()}")
            coins = coins.sortedBy {
                Log.d("TAGT", "load5 : ${Thread.currentThread().name}")
                it.marketCapRank }

            val endOfPaginationReached = coins.isEmpty()
            coinDatabase.withTransaction {
                // clear all tables in the database
                Log.d("TAGT", "load2 : ${Thread.currentThread().name}")
                if (loadType == LoadType.REFRESH) {
                    coinDatabase.remoteKeysDao.clearRemoteKeys()
                    coinDatabase.coinDatabaseDao.clearAll()
                    Log.d("TAG", "Clear All")
                }
                val prevKey = if (page == GITHUB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = coins.map {
                    RemoteKeys(coinId = it.id ?: "null", prevKey = prevKey, nextKey = nextKey)
                }
                coinDatabase.remoteKeysDao.insertAll(keys)
                coinDatabase.coinDatabaseDao.insertAll(coins.map {
                    Coin(
                        coinId = it.id.toString(),
                        marketCapRank = it.marketCapRank,
                        coinName = it.name,
                        coinSymbol = it.symbol?.toUpperCase(Locale.ROOT),
                        priceChangePercentage24h = it.priceChangePercentage24h,
                        currentPrice = it.currentPrice,
                        image = it.image
                    )
                })
            }
            when (loadType) {
                LoadType.REFRESH -> {
                    Log.d(
                        "TAG",
                        "successful refresh endOfPaginationReached=$endOfPaginationReached"
                    )
                }
                LoadType.PREPEND -> {
                    Log.d(
                        "TAG",
                        "successful prepend endOfPaginationReached=$endOfPaginationReached"
                    )
                }
                LoadType.APPEND -> {
                    Log.d("TAG", "successful append endOfPaginationReached=$endOfPaginationReached")
                }
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: CoinGeckoApiException) {
            Log.d("TAG", exception.message)
            return MediatorResult.Error(exception)
        } catch (exception: CancellationException) {
            exception.message?.let { Log.d("TAG", it) }
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Coin>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { coin ->
                // Get the remote keys of the last item retrieved
                coinDatabase.remoteKeysDao.get(coin.coinId)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Coin>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { coin ->
                // Get the remote keys of the first items retrieved
                coinDatabase.remoteKeysDao.get(coin.coinId)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Coin>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { coin ->
                coinDatabase.remoteKeysDao.get(coin.coinId)
            }
        }
    }
}
