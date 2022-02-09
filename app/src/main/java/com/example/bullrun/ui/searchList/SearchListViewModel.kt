package com.example.bullrun.ui.searchList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.bullrun.data.repos.coin.CoinRepository
import com.example.bullrun.data.database.model.CoinList

class SearchListViewModel(application: Application) : AndroidViewModel(application) {

    private val coinRepository = CoinRepository.getRepository(application)

    val searchItems=MutableLiveData<List<CoinList>>()

    suspend fun getSearchItems(query:String){
        searchItems.value=coinRepository.getAllCoinsList(query)
    }

}