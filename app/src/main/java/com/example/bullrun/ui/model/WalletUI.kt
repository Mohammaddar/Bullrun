package com.example.bullrun.ui.model

import com.example.bullrun.data.database.model.Wallet

data class WalletUI(
    var walletName: String,
    val walletBalance: Double = 0.0
){
    companion object {
        fun transformDataModelToUiModel(ls: List<Wallet>): List<WalletUI> {
            return ls.map {
                WalletUI(it.walletName)
            }
        }
    }
}