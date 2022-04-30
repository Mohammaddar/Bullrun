package com.example.bullrun.ui.model

data class TrendingCoin(
    var coinId: String? = "bitcoin",
    val coinName: String? = "Bitcoin",
    val symbol: String? = "BTC",
    val score: Int = 0,
    val image: String? = null,
    private val priceBTC: Double = 0.0,
) {
    val price = "${String.format("%.6f", priceBTC)} ${
        priceBTC.toString().substring(
            if (priceBTC.toString().indexOf("E") != -1) {
                priceBTC.toString().indexOf("E")
            } else {
                priceBTC.toString().length
            }, priceBTC.toString().length
        )
    }"
}

