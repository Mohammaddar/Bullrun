package com.example.bullrun.ui.mainList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.bullrun.data.repos.coin.CoinRepository
import com.example.bullrun.data.database.model.Coin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainListViewModel(application: Application) : AndroidViewModel(application) {


    private val coinRepository = CoinRepository.getRepository(application)
    val coinsList: Flow<PagingData<Coin>> =
        coinRepository.getCoinsStream("").cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            if (!coinRepository.isCoinsListAlreadyLoaded()) {
                coinRepository.getAllCoinsListAndInsertToDB()
                Log.d("TAG", "done")
            }
            else
                Log.d("TAG", "no need")
        }
    }

}