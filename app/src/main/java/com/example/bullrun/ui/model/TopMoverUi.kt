package com.example.bullrun.ui.model

data class TopMoverUi(
    var coinId: String?="bitcoin",
    val coinName: String?="Bitcoin",
    val symbol: String?="BTC",
    val changePercentage: Double=0.0,
    val image: String? = null,
    val currentPrice: Double = 0.0,
)