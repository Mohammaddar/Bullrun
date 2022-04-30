package com.example.bullrun.ui.fragments.coinInfo

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.bullrun.data.database.model.Coin
import com.example.bullrun.data.repos.coin.CoinRepository
import com.example.bullrun.ui.model.CoinCommunityUi
import com.example.bullrun.ui.model.CoinInfoUI
import com.example.bullrun.ui.model.CoinStatisticUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.ArrayIndexOutOfBoundsException

class CoinInfoViewModel(application: Application, val coinId: String) :
    AndroidViewModel(application) {

    private val coinRepository = CoinRepository.getRepository(application)

    val coinInfo = MutableLiveData<CoinInfoUI>()

    val statistics = coinInfo.map { coinInfo ->
        val ls = mutableListOf(
            CoinStatisticUi("mCapRank", coinInfo.mCapRank),
            CoinStatisticUi("mCap", coinInfo.mCapString),
            CoinStatisticUi("totalSupply", coinInfo.totalSupplyString),
            CoinStatisticUi("circulatingSupply", coinInfo.circulatingSupplyString),
            CoinStatisticUi("roi", coinInfo.roiString),
            CoinStatisticUi("score", coinInfo.score),
        )
        ls.filter { it.value != null }
    }

    val community = coinInfo.map { coinInfo ->
        val ls = mutableListOf<CoinCommunityUi>()
        coinInfo.communityData?.let {
            if (it.facebookLikes != 0.0) {
                val facebookURL="https://facebook.com/${coinInfo.links.facebookUsername}"
                ls.add(CoinCommunityUi("Facebook Likes", it.facebookLikes,facebookURL))
            }
            if (it.twitterFollowers != 0.0) {
                val twitterURL="https://twitter.com/${coinInfo.links.twitterScreenName}"
                ls.add(CoinCommunityUi("Twitter Followers", it.twitterFollowers,twitterURL))
            }
            if (it.redditSubscribers != 0.0) {
                val redditURL="${coinInfo.links.subredditUrl}"
                ls.add(CoinCommunityUi("Reddit Subscribers", it.redditSubscribers,redditURL))
            }
            if (it.telegramChannelUserCount != null && it.telegramChannelUserCount != 0.0) {
                val telegramURL="https://t.me/${coinInfo.links.telegramChannelIdentifier}"
                ls.add(CoinCommunityUi("Telegram Channel User Count", it.telegramChannelUserCount!!,telegramURL))
            }
        }
        return@map ls
    }

    val chartPricesLiveData = MutableLiveData<HashMap<String, List<Double>>>()

    var timeFrame = MutableLiveData<String>()

    val med = MediatorLiveData<String>()

    private var lastOnlineFetchTime = 0L

    init {
        med.addSource(timeFrame) {
            Log.d("TAGDE", "change timeFrame")
            med.value = "new value"
        }
        med.addSource(chartPricesLiveData) {
            Log.d("TAGDE", "change marketChartLiveData")
            med.value = "new value"
        }
        fetchChart(coinId)
        getFullCoinInfo()
    }

    private fun getFullCoinInfo() {
        viewModelScope.launch {
            coinInfo.value = coinRepository.getCoinInfoByID(
                coinId = coinId,
                tickers = false,
                communityData = true,
                developerData = true,
                sparkline = false
            )
                ?.let { CoinInfoUI.transformDataModelToUiModel(it) }
        }
    }

    fun getSmallCoinInfo(): Coin? {
        val info = coinInfo.value;
        return info?.let {
            Coin(
                coinId = it.id,
                marketCapRank = it.mCapRank,
                coinName = it.name,
                priceChangePercentage24h = it.priceChangePercentage,
                currentPrice = it.currentPrice,
                image = it.image
            )
        }
    }

    private fun fetchChart(coinId: String) {
        if (chartPricesLiveData.value == null || System.currentTimeMillis() > lastOnlineFetchTime + (60 * 1000)) {
            viewModelScope.launch {
                val chartPrices1D = mutableListOf<Double>()
                val chartPrices1W = mutableListOf<Double>()
                val chartPrices1M = mutableListOf<Double>()
                val chartPrices3M = mutableListOf<Double>()

                val jobFetchAndModelData = launch(Dispatchers.Default) {
                    lateinit var chartPrices: List<Double>
                    val jobFetchFromServer = launch {
                        launch {
                            coinRepository.getChartByID(coinId, days = 90.0)
                                ?.let { chartPrices = it.prices.map { p -> p[1].toDouble() } }

                        }
                    }
                    jobFetchFromServer.join()
                    lastOnlineFetchTime = System.currentTimeMillis()
                    // }

                    val chartPricesSize = chartPrices.size


                    for (i in 1..24) {
                        try {
                            chartPrices1D.add(chartPrices[chartPricesSize - 1 - i])
                        } catch (ex: ArrayIndexOutOfBoundsException) {
                            chartPrices1D.add(0.0)
                        }
                    }

                    for (i in 1..28) {
                        try {
                            chartPrices1W.add(chartPrices[chartPricesSize - 2 - ((i - 1) * 6)])
                        } catch (ex: ArrayIndexOutOfBoundsException) {
                            chartPrices1W.add(0.0)
                        }
                    }

                    for (i in 1..30) {
                        try {
                            chartPrices1M.add(chartPrices[chartPricesSize - 2 - ((i - 1) * 24)])
                        } catch (ex: ArrayIndexOutOfBoundsException) {
                            chartPrices1M.add(0.0)
                        }
                    }

                    for (i in 1..30) {
                        try {
                            chartPrices3M.add(chartPrices[chartPricesSize - 2 - ((i - 1) * 72)])
                        } catch (ex: ArrayIndexOutOfBoundsException) {
                            chartPrices3M.add(0.0)
                        }
                    }
                }
                jobFetchAndModelData.join()

                chartPricesLiveData.value = hashMapOf(
                    "1D" to chartPrices1D,
                    "1W" to chartPrices1W,
                    "1M" to chartPrices1M,
                    "3M" to chartPrices3M
                )
            }
        }
    }

}