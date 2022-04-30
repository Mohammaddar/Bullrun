package com.example.bullrun.ui.model

val titles = hashMapOf(
    "mCapRank" to "Market Cap Rank",
    "mCap" to "Market Cap",
    "totalSupply" to "Total Supply",
    "circulatingSupply" to "Circulating Supply",
    "roi" to "ROI(Return Of Investment)",
    "score" to "Score"
)

val descs = hashMapOf(
    "mCap" to "Market Cap = Current Price x Circulating Supply\n\n" +
            "Refers to the total market value of a cryptocurrency’s circulating supply." +
            " It is similar to the stock market’s measurement of multiplying price per share by shares readily available in the market (not held & locked by insiders, governments)",
    "totalSupply" to "Max Supply = Theoretical maximum as coded\n\n" +
            "The amount of coins that have already been created, minus any coins that have been burned (removed from circulation). It is comparable to outstanding shares in the stock market.",
    "circulatingSupply" to "The amount of coins that are circulating in the market and are tradeable by the public. It is comparable to looking at shares readily available in the market (not held & locked by insiders, governments).",
    "roi" to "ROI is a metric to measure the performance and the efficacy of a crypto investment," +
            " or to compare the performance of multiple crypto investments in a portfolio."
)

data class CoinStatisticUi(
    val id: String,
    val value: Any?
) {
    val title: String = titles[id] ?: ""
    val desc:String?= descs[id]
}