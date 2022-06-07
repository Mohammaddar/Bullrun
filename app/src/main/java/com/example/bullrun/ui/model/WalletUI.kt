package com.example.bullrun.ui.model

import com.example.bullrun.decimalCount

data class WalletUI(
    var name: String,
    val balance: Double = 0.0,
    val holdingsInProfit: Int = 0,
    val holdingsInLoss: Int = 0,
    val holdingsCount: Int = 0,
    val pnlPercentage: Double = 0.0,
    val pnlValue: Double = 0.0,
) {

    val holdingsCountText =
        "There are $holdingsInProfit holdings in profit and $holdingsInLoss holdings in loss."
    val holdingsPnlText = if (pnlPercentage >= 0) {
        "Overall, your holdings have grown $pnlPercentage % which is about $pnlValue."
    } else {
        "Overall, your holdings have dropped $pnlPercentage % which is about $pnlValue."
    }

    companion object {

        fun inflateWalletWithHoldingsData(walletName: String, holdings: List<HoldingUI>): WalletUI {
            var balance = 0.0
            var hip = 0
            var hil = 0
            var hc = 0
            var pnlV = 0.0
            var pnlP = 0.0
            for (holding in holdings) {
                balance += holding.totalHoldingValue
                if (holding.pnlPercentage >= 0) hip++
                else hil++
                hc++
                pnlV += holding.pnlValue
            }
            if (hc != 0) pnlP = (pnlV / (balance - pnlV))*100

            return WalletUI(
                name = walletName,
                balance = balance.decimalCount(2),
                holdingsInProfit = hip,
                holdingsInLoss = hil,
                holdingsCount = hc,
                pnlPercentage = pnlP.decimalCount(2),
                pnlValue = pnlV.decimalCount(2)
            )
        }
    }
}