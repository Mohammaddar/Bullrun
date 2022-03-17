package com.example.bullrun.ui.activitywallet

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.bullrun.data.repos.portfolio.PortfolioRepository
import com.example.bullrun.ui.model.AssetUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WalletViewModel(application: Application) : AndroidViewModel(application) {


    private val portfolioRepository = PortfolioRepository.getRepository(application)

    var walletName = MutableStateFlow("")

    var assetsList = MutableLiveData<MutableList<AssetUI>>()


    init {
        viewModelScope.launch() {
            Log.d("TAGP", "launch : ${Thread.currentThread().name}")
            walletName.collectLatest { it ->
                Log.d("TAGP", "collect : ${Thread.currentThread().name}")
                if (it != "") {
                    Log.d("TAGP", "unempty wallet name : ${Thread.currentThread().name}")
                    portfolioRepository.getAllAssetsInWallet(it).distinctUntilChanged()
                        .map { it2 ->
                            Log.d("TAGP", "map : ${Thread.currentThread().name}")
                            delay(350)
                            AssetUI.transformDataModelToUiModel(it2, application).toMutableList()
                        }.flowOn(Dispatchers.Default).collect { it3 ->//TODO change to colect latest
                            Log.d("TAGP", "collect2: ${Thread.currentThread().name}")
                            assetsList.value = it3
                            portfolioRepository.updateAssetsInfo(it3.map { it1 -> it1.coinId })//TODO see if you can refactor this
                            fetchChart(it3)
                            Log.d("TAGP", "collect3: ${Thread.currentThread().name}")
                        }
                }
            }
        }
    }

   fun fetchChart(assetList:List<AssetUI>){
       for (asset in assetList){

       }
   }

}