package com.example.bullrun.ui.activitywallet

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bullrun.data.database.model.Transaction
import com.example.bullrun.data.repos.coin.CoinRepository
import com.example.bullrun.data.repos.portfolio.PortfolioRepository
import com.example.bullrun.ui.model.AssetUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WalletViewModel(application: Application) : AndroidViewModel(application) {


    private val portfolioRepository = PortfolioRepository.getRepository(application)
    private val coinRepository = CoinRepository.getRepository(application)

    var walletName = MutableStateFlow("")

    var timeFrame = MutableLiveData<String>()

    var assetsList = MutableLiveData<MutableList<AssetUI>>()

    val chartsDataset = MutableLiveData<HashMap<String, MutableList<Double>>>()

    val med = MediatorLiveData<String>()


    init {
        med.addSource(timeFrame) {
            Log.d("TAGDE", "change timeFrame")
            med.value = "new value"
        }
        med.addSource(chartsDataset) {
            Log.d("TAGDE", "change chartsDataset")
            med.value = "new value"
        }

        viewModelScope.launch() {
            Log.d("TAGP", "launch : ${Thread.currentThread().name}")
            walletName.collectLatest { walletName ->
                Log.d("TAGP", "collect : ${Thread.currentThread().name}")
                if (walletName != "") {
                    //update assets prices first
                    Log.d("TAGP", "update : ${Thread.currentThread().name}")
                    portfolioRepository.updateAssetsInfo(
                        portfolioRepository.getAllAssetsIDsInWallet(
                            walletName
                        )
                    )

                    Log.d("TAGP", "unempty wallet name : ${Thread.currentThread().name}")
                    portfolioRepository.getAllAssetsInWallet(walletName).distinctUntilChanged()
                        .map { it2 ->
                            Log.d("TAGP", "map : ${Thread.currentThread().name}")
                            delay(350)
                            AssetUI.transformDataModelToUiModel(it2, application).toMutableList()
                        }.flowOn(Dispatchers.Default).collectLatest { it3 ->
                            Log.d("TAGP", "collect2: ${Thread.currentThread().name}")
                            assetsList.value = it3
                            //portfolioRepository.updateAssetsInfo(it3.map { it1 -> it1.coinId })//TODO see if you can refactor this
                            fetchChart(it3)
                            Log.d("TAGP", "collect3: ${Thread.currentThread().name}")
                        }
                }
            }


        }
    }

    private val assetChartPricesPairs = mutableListOf<Pair<String, List<List<String>>>>()
    var lastOnlineFetchTime: Long = 0

    suspend fun fetchChart(assetList: List<AssetUI>) {
        if (assetChartPricesPairs.isEmpty() || System.currentTimeMillis() > lastOnlineFetchTime + (60 * 1000)) {
            val job = viewModelScope.launch(Dispatchers.IO) {
                for (asset in assetList) {
                    launch {
                        Log.d("TAGCH", "${asset.coinId} start: ${Thread.currentThread().name}")
                        coinRepository.getChartByID(asset.coinId, days = 90.0)
                            ?.let { assetChartPricesPairs.add(asset.coinId to it.prices) }
                        Log.d("TAGCH", "${asset.coinId} end: ${Thread.currentThread().name}")
                    }
                }
            }
            job.join()
            lastOnlineFetchTime = System.currentTimeMillis()

            Log.d("TAGCH", "chart $assetChartPricesPairs ${Thread.currentThread().name}")


            val chartPrices1D = mutableListOf<Double>()
            val chartPrices1W = mutableListOf<Double>()
            val chartPrices1M = mutableListOf<Double>()
            val chartPrices3M = mutableListOf<Double>()

            for ((assetName, chartPrices) in assetChartPricesPairs) {
                Log.d("TAGDE", "viewmodel chartPrices ${chartPrices.size}")
                val transactions =
                    portfolioRepository.getTransactionsByAssetAndWallet(assetName, walletName.value)?:return



                kotlin.run {
                    var currentVol = assetList.first { it.coinId == assetName }.netHoldingVolume
                    var currentPrice: Double
                    var currentTimeMillis: Long
                    var lastTimeMillis = chartPrices[chartPrices.size - 1][0].toLong()
                    val validTransactions = mutableListOf<Transaction>()

                    for (i in 1..24) {
                        currentPrice = chartPrices[chartPrices.size - 1 - i][1].toDouble()
                        currentTimeMillis = chartPrices[chartPrices.size - 1 - i][0].toLong()
                        validTransactions.clear()
                        validTransactions.addAll(transactions.filter { it.dateMillis in (currentTimeMillis + 1)..lastTimeMillis })
                        for (transaction in validTransactions) {
                            if (transaction.type == "Buy") {
                                currentVol -= transaction.volume
                            } else {//Sell
                                currentVol += transaction.volume
                            }
                        }
                        if (chartPrices1D.size < 24) {
                            chartPrices1D.add(currentVol * currentPrice)
                        } else {
                            chartPrices1D[i - 1] = chartPrices1D[i - 1] + currentVol * currentPrice
                        }
                        //chartPrices1D.add(currentTimeMillis to currentVol * currentPrice)
                        lastTimeMillis = currentTimeMillis
                    }
                }

                kotlin.run {
                    var currentVol = assetList.first { it.coinId == assetName }.netHoldingVolume
                    var currentPrice: Double
                    var currentTimeMillis: Long
                    var lastTimeMillis = chartPrices[chartPrices.size - 1][0].toLong()
                    val validTransactions = mutableListOf<Transaction>()

                    for (i in 1..28) {
                        currentPrice =
                            chartPrices[chartPrices.size - 2 - ((i - 1) * 6)][1].toDouble()
                        currentTimeMillis =
                            chartPrices[chartPrices.size - 2 - ((i - 1) * 6)][0].toLong()
                        validTransactions.clear()
                        validTransactions.addAll(transactions.filter { it.dateMillis in (currentTimeMillis + 1)..lastTimeMillis })
                        for (transaction in validTransactions) {
                            if (transaction.type == "Buy") {
                                currentVol -= transaction.volume
                            } else {//Sell
                                currentVol += transaction.volume
                            }
                        }
                        if (chartPrices1W.size < 28) {
                            chartPrices1W.add(currentVol * currentPrice)
                        } else {
                            chartPrices1W[i - 1] = chartPrices1W[i - 1] + currentVol * currentPrice
                        }
                        //chartPrices1W.add(currentTimeMillis to currentVol * currentPrice)
                        lastTimeMillis = currentTimeMillis
                    }
                }

                kotlin.run {
                    var currentVol = assetList.first { it.coinId == assetName }.netHoldingVolume
                    var currentPrice: Double
                    var currentTimeMillis: Long
                    var lastTimeMillis = chartPrices[chartPrices.size - 1][0].toLong()
                    val validTransactions = mutableListOf<Transaction>()

                    for (i in 1..30) {
                        currentPrice =
                            chartPrices[chartPrices.size - 2 - ((i - 1) * 24)][1].toDouble()
                        currentTimeMillis =
                            chartPrices[chartPrices.size - 2 - ((i - 1) * 24)][0].toLong()
                        validTransactions.clear()
                        validTransactions.addAll(transactions.filter { it.dateMillis in (currentTimeMillis + 1)..lastTimeMillis })
                        for (transaction in validTransactions) {
                            if (transaction.type == "Buy") {
                                currentVol -= transaction.volume
                            } else {//Sell
                                currentVol += transaction.volume
                            }
                        }
                        if (chartPrices1M.size < 30) {
                            chartPrices1M.add(currentVol * currentPrice)
                        } else {
                            chartPrices1M[i - 1] = chartPrices1M[i - 1] + currentVol * currentPrice
                        }
                        //chartPrices1M.add(currentTimeMillis to currentVol * currentPrice)
                        lastTimeMillis = currentTimeMillis
                    }
                }

                kotlin.run {
                    var currentVol = assetList.first { it.coinId == assetName }.netHoldingVolume
                    var currentPrice: Double
                    var currentTimeMillis: Long
                    var lastTimeMillis = chartPrices[chartPrices.size - 1][0].toLong()
                    val validTransactions = mutableListOf<Transaction>()

                    for (i in 1..30) {
                        currentPrice =
                            chartPrices[chartPrices.size - 2 - ((i - 1) * 72)][1].toDouble()
                        currentTimeMillis =
                            chartPrices[chartPrices.size - 2 - ((i - 1) * 72)][0].toLong()
                        validTransactions.clear()
                        validTransactions.addAll(transactions.filter { it.dateMillis in (currentTimeMillis + 1)..lastTimeMillis })
                        for (transaction in validTransactions) {
                            if (transaction.type == "Buy") {
                                currentVol -= transaction.volume
                            } else {//Sell
                                currentVol += transaction.volume
                            }
                        }
                        if (chartPrices3M.size < 30) {
                            chartPrices3M.add(currentVol * currentPrice)
                        } else {
                            chartPrices3M[i - 1] = chartPrices3M[i - 1] + currentVol * currentPrice
                        }
                        Log.d(
                            "TAGF",
                            "$assetName  $currentVol $currentPrice ${currentVol * currentPrice}"
                        )
                        //chartPrices3M.add(currentTimeMillis to currentVol * currentPrice)
                        lastTimeMillis = currentTimeMillis
                    }
                }
            }
            chartsDataset.value = hashMapOf(
                "1D" to chartPrices1D,
                "1W" to chartPrices1W,
                "1M" to chartPrices1M,
                "3M" to chartPrices3M
            )
            Log.d("TAGDE", "viewmodel ${chartPrices1D.size}")
            Log.d("TAGDE", "viewmodel $chartPrices3M")
        }
    }

//    suspend fun fetchChart(assetList: List<AssetUI>) {
//        val assetChartPricesPairs = mutableListOf<Pair<String, List<List<String>>>>()
//        val job = viewModelScope.launch(Dispatchers.IO) {
//            for (asset in assetList) {
//                launch {
//                    Log.d("TAGCH", "${asset.coinId} start: ${Thread.currentThread().name}")
//                    coinRepository.getChartByID(asset.coinId)
//                        ?.let { assetChartPricesPairs.add(asset.coinId to it.prices) }
//                    Log.d("TAGCH", "${asset.coinId} end: ${Thread.currentThread().name}")
//                }
//            }
//        }
//        job.join()
//
//        Log.d("TAGCH", "chart $assetChartPricesPairs ${Thread.currentThread().name}")
//
//        for ((assetName, chartPrices) in assetChartPricesPairs) {
//            var currentVol = assetList.filter { it.coinName == assetName }[0].netHoldingVolume
//            val transactions =
//                portfolioRepository.getTransactionsByAssetAndWallet(assetName, walletName.value)
//
//            val chartPrices1D = mutableListOf<Pair<Long, Double>>()
//            kotlin.run {
//                var currentPrice: Double
//                var currentTimeMillis: Long
//                var lastTimeMillis = chartPrices[chartPrices.size - 1][0].toLong()
//                val validTransactions = mutableListOf<Transaction>()
//
//                for (i in 1..24) {
//                    currentPrice = chartPrices[chartPrices.size - 1 - i][1].toDouble()
//                    currentTimeMillis = chartPrices[chartPrices.size - 1 - i][0].toLong()
//                    validTransactions.clear()
//                    validTransactions.addAll(transactions.filter { it.dateMillis in (currentTimeMillis + 1)..lastTimeMillis })
//                    for (transaction in validTransactions) {
//                        if (transaction.type == "Buy") {
//                            currentVol -= transaction.volume
//                        } else {//Sell
//                            currentVol += transaction.volume
//                        }
//                    }
//                    chartPrices1D.add(currentTimeMillis to currentVol * currentPrice)
//                    lastTimeMillis = currentTimeMillis
//                }
//            }
//
//            val chartPrices1W = mutableListOf<Pair<Long, Double>>()
//            kotlin.run {
//                var currentPrice: Double
//                var currentTimeMillis: Long
//                var lastTimeMillis = chartPrices[chartPrices.size - 1][0].toLong()
//                val validTransactions = mutableListOf<Transaction>()
//
//                for (i in 1..28) {
//                    currentPrice = chartPrices[chartPrices.size - 2 - ((i - 1) * 6)][1].toDouble()
//                    currentTimeMillis =
//                        chartPrices[chartPrices.size - 2 - ((i - 1) * 6)][0].toLong()
//                    validTransactions.clear()
//                    validTransactions.addAll(transactions.filter { it.dateMillis in (currentTimeMillis + 1)..lastTimeMillis })
//                    for (transaction in validTransactions) {
//                        if (transaction.type == "Buy") {
//                            currentVol -= transaction.volume
//                        } else {//Sell
//                            currentVol += transaction.volume
//                        }
//                    }
//                    chartPrices1W.add(currentTimeMillis to currentVol * currentPrice)
//                    lastTimeMillis = currentTimeMillis
//                }
//            }
//
//            val chartPrices1M = mutableListOf<Pair<Long, Double>>()
//            kotlin.run {
//                var currentPrice: Double
//                var currentTimeMillis: Long
//                var lastTimeMillis = chartPrices[chartPrices.size - 1][0].toLong()
//                val validTransactions = mutableListOf<Transaction>()
//
//                for (i in 1..30) {
//                    currentPrice = chartPrices[chartPrices.size - 2 - ((i - 1) * 24)][1].toDouble()
//                    currentTimeMillis =
//                        chartPrices[chartPrices.size - 2 - ((i - 1) * 24)][0].toLong()
//                    validTransactions.clear()
//                    validTransactions.addAll(transactions.filter { it.dateMillis in (currentTimeMillis + 1)..lastTimeMillis })
//                    for (transaction in validTransactions) {
//                        if (transaction.type == "Buy") {
//                            currentVol -= transaction.volume
//                        } else {//Sell
//                            currentVol += transaction.volume
//                        }
//                    }
//                    chartPrices1M.add(currentTimeMillis to currentVol * currentPrice)
//                    lastTimeMillis = currentTimeMillis
//                }
//            }
//
//            val chartPrices3M = mutableListOf<Pair<Long, Double>>()
//            kotlin.run {
//                var currentPrice: Double
//                var currentTimeMillis: Long
//                var lastTimeMillis = chartPrices[chartPrices.size - 1][0].toLong()
//                val validTransactions = mutableListOf<Transaction>()
//
//                for (i in 1..30) {
//                    currentPrice = chartPrices[chartPrices.size - 2 - ((i - 1) * 72)][1].toDouble()
//                    currentTimeMillis =
//                        chartPrices[chartPrices.size - 2 - ((i - 1) * 72)][0].toLong()
//                    validTransactions.clear()
//                    validTransactions.addAll(transactions.filter { it.dateMillis in (currentTimeMillis + 1)..lastTimeMillis })
//                    for (transaction in validTransactions) {
//                        if (transaction.type == "Buy") {
//                            currentVol -= transaction.volume
//                        } else {//Sell
//                            currentVol += transaction.volume
//                        }
//                    }
//                    chartPrices3M.add(currentTimeMillis to currentVol * currentPrice)
//                    lastTimeMillis = currentTimeMillis
//                }
//            }
//
//            chartsDataset.clear()
//            chartsDataset.add(
//                assetName to listOf(
//                    "1D" to chartPrices1D,
//                    "1W" to chartPrices1W,
//                    "1M" to chartPrices1M,
//                    "3M" to chartPrices3M
//                )
//            )
//        }
//
//    }


}