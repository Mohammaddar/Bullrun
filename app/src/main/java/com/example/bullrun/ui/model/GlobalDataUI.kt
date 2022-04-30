package com.example.bullrun.ui.model

import com.example.bullrun.decimalCount
import drewcarlson.coingecko.models.global.GlobalData

data class GlobalDataUI(
    private var _totalMarketCap: Double?,
    private var _dominanceBtc: Double?,
    private var _dominanceEth: Double?
) {
    var totalMarketCap = _totalMarketCap?.decimalCount(2)
    var dominanceBtc = _dominanceBtc?.decimalCount(2)
    var dominanceEth = _dominanceEth?.decimalCount(2)

    companion object {
        fun transformDataModelToUiModel(gd: GlobalData): GlobalDataUI {
            return GlobalDataUI(
                gd.totalMarketCap?.get("usd"),
                gd.marketCapPercentage?.get("btc"),
                gd.marketCapPercentage?.get("eth")
            )
        }
    }
}

