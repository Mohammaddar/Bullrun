package com.example.bullrun.ui.fragments.portfolio

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bullrun.data.database.model.Wallet
import com.example.bullrun.data.repos.portfolio.PortfolioRepository
import com.example.bullrun.ui.model.AssetUI
import com.example.bullrun.ui.model.WalletUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {


    private val portfolioRepository = PortfolioRepository.getRepository(application)


    var walletList = portfolioRepository.getAllWallets()
        .map { WalletUI.transformDataModelToUiModel(it) }
        .flowOn(Dispatchers.IO).asLiveData()

    init {
    }

    suspend fun addWallet(walletName: String) {
        portfolioRepository.addNewWallet(Wallet(walletName))
    }
}