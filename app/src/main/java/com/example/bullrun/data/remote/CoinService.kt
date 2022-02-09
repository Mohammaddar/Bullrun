package com.example.bullrun.data.remote

import drewcarlson.coingecko.CoinGeckoClient
import drewcarlson.coingecko.models.coins.CoinFullData
import drewcarlson.coingecko.models.coins.CoinList
import drewcarlson.coingecko.models.coins.CoinMarkets

class CoinService {

    private val coinGecko = CoinGeckoClient.create()

    companion object {

        @Volatile
        private var INSTANCE: CoinService? = null

        fun getInstance(): CoinService {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance=CoinService()
                }
                return instance
            }
        }
    }

    suspend fun getCoins(query:String, page:Int, pageSize:Int):List<CoinMarkets>{
        return coinGecko.getCoinMarkets(vsCurrency = "usd",priceChangePercentage = "24h",page = page,perPage = pageSize).markets
    }

    suspend fun getAllCoinsList():List<CoinList>{
        return coinGecko.getCoinList()
    }

    suspend fun getCoinByID(coinId:String):CoinFullData{
        return coinGecko.getCoinById(id = coinId,marketData = true,tickers = false,communityData = false,developerData = false,sparkline =false);
    }

    suspend fun getCoinsByIDs(ids:List<String>):List<CoinMarkets>{
        val idsStr=""
        ids.forEach { idsStr.plus("$it,") }
        return coinGecko.getCoinMarkets(vsCurrency = "usd",ids=idsStr).markets
    }
}