package com.example.bullrun.data.remote

import drewcarlson.coingecko.CoinGeckoClient
import drewcarlson.coingecko.models.coins.CoinFullData
import drewcarlson.coingecko.models.coins.CoinList
import drewcarlson.coingecko.models.coins.CoinMarkets
import drewcarlson.coingecko.models.coins.MarketChart
import drewcarlson.coingecko.models.global.Global
import drewcarlson.coingecko.models.global.GlobalData
import drewcarlson.coingecko.models.search.TrendingCoin

class CoinService {

    private val coinGecko = CoinGeckoClient.create()

    companion object {

        @Volatile
        private var INSTANCE: CoinService? = null

        fun getInstance(): CoinService {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = CoinService()
                }
                return instance
            }
        }
    }

    suspend fun getCoins(query: String, page: Int, pageSize: Int): List<CoinMarkets> {
        return coinGecko.getCoinMarkets(
            vsCurrency = "usd",
            priceChangePercentage = "24h",
            page = page,
            perPage = pageSize
        ).markets
    }

    suspend fun getAllCoinsList(): List<CoinList> {
        return coinGecko.getCoinList()
    }

    suspend fun getCoinByID(coinId: String,tickers: Boolean,communityData: Boolean,developerData: Boolean,sparkline: Boolean): CoinFullData {
        return coinGecko.getCoinById(
            id = coinId,
            marketData = true,
            tickers = tickers,
            communityData = communityData,
            developerData = developerData,
            sparkline = sparkline
        );
    }

    suspend fun getCoinsByIDs(ids: List<String>): List<CoinMarkets> {
        val idsStr = ""
        ids.forEach { idsStr.plus("$it,") }
        return coinGecko.getCoinMarkets(vsCurrency = "usd", ids = idsStr).markets
    }

    suspend fun getMarketChart(coinId: String,days:Double): MarketChart {
        return coinGecko.getCoinMarketChartById(coinId, "usd", days)
    }

    suspend fun getTopCoinMarketChart(): MarketChart {
        val topCoinID=coinGecko.getCoinMarkets(vsCurrency = "usd", order ="market_cap_desc", perPage = 1, page = 1 ).markets[0].id
        return coinGecko.getCoinMarketChartById(id=topCoinID, vsCurrency = "usd",days=2.0)//cant use 1day.1 day time frame has minutely interval
    }

    suspend fun getTop250Coins(): List<CoinMarkets> {
        return coinGecko.getCoinMarkets(
            vsCurrency = "usd",
            perPage = 250,
            page = 0,
            priceChangePercentage = "24h"
        ).markets
    }

    suspend fun getTrendingCoins(): List<TrendingCoin> {
        return coinGecko.getTrending().coins
    }

    suspend fun getGlobalData(): GlobalData {
        return coinGecko.getGlobal().data
    }

}