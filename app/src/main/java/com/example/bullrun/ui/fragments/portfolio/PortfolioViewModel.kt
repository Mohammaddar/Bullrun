package com.example.bullrun.ui.fragments.portfolio

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bullrun.data.database.model.Wallet
import com.example.bullrun.data.repos.coin.CoinRepository
import com.example.bullrun.data.repos.portfolio.PortfolioRepository
import com.example.bullrun.ui.model.HoldingUI
import com.example.bullrun.ui.model.WalletUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {


    private val portfolioRepository = PortfolioRepository.getRepository(application)

    var walletsList = MutableLiveData<List<WalletUI>>()

    init {
        viewModelScope.launch {
            portfolioRepository.getAllWalletsNamesFlow().distinctUntilChanged().collectLatest { walletNames ->
                    portfolioRepository.updateAllAssetsInfo()
                    portfolioRepository.getAllHoldingAssetsFlow()
                        .map { assets -> HoldingUI.transformDataModelToUiModel(assets) }
                        .map { holdings ->
                            val wallets = mutableListOf<WalletUI>()
                            for (walletName in walletNames) {
                                wallets.add(
                                    WalletUI.inflateWalletWithHoldingsData(
                                        walletName,
                                        holdings.filter { h->h.walletName==walletName }
                                    )
                                )
                            }
                            return@map wallets
                        }.flowOn(Dispatchers.Default).collectLatest {
                            walletsList.value = it
                        }
                }
        }
    }

    suspend fun addWallet(walletName: String) {
        portfolioRepository.addNewWallet(Wallet(walletName))
    }
}