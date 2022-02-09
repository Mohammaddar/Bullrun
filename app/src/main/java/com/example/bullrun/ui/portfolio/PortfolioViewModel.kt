package com.example.bullrun.ui.portfolio

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bullrun.data.repos.portfolio.PortfolioRepository
import com.example.bullrun.ui.model.AssetUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {


    private val portfolioRepository = PortfolioRepository.getRepository(application)
    var assetsList = MutableLiveData<MutableList<AssetUI>>()

    init {
        viewModelScope.launch() {

            portfolioRepository.getAllAssets().distinctUntilChanged()
                .map {
                    Log.d("TAGP", "thread1 : ${Thread.currentThread().name}")
                    delay(350)
                    AssetUI.transformDataModelToUiModel(it).toMutableList()
                }.flowOn(Dispatchers.Default).collect {
                    Log.d("TAGP", "thread2: ${Thread.currentThread().name}")
                    assetsList.value = it
                    Log.d("TAGP", "thread5: ${Thread.currentThread().name}")
                    portfolioRepository.updateAssetsInfo(it.map { it1 -> it1.coinId })
                    Log.d("TAGP", "thread7: ${Thread.currentThread().name}")
                }

        }
    }
}