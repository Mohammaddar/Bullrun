package com.example.bullrun.ui.model

import com.example.bullrun.decimalCount
import drewcarlson.coingecko.models.coins.CoinFullData
import drewcarlson.coingecko.models.coins.data.CommunityData
import drewcarlson.coingecko.models.coins.data.Links
import java.text.DateFormatSymbols
import java.text.NumberFormat

data class CoinInfoUI(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String?,
    val mCapRank: Long,
    val currentPrice: Double?,
    private val _priceChangePercentage: Double?,
    private val _mCap: Double?,
    //val maxSupply:Double,
    private val _totalSupply: Double?,
    private val _circulatingSupply: Double?,
//    val fullyDilutedValuation: Double,
//    val totalValueLocked: Double,
//    val fdvPerTvl: Double,
//    val mCapPerTvl: Double,
    val _roi: Float?,
    val _score: Double?,
    val desc: String?,
    //val miniDesc:String,
    val low24H: Double?,
    val high24H: Double?,
    val ath: Double?,
    val athPriceChangePercentage: Double?,
    private val _athDate: String?,
    val communityData: CommunityData?,
    val links:Links

) {

    val mCapString: String? = if (_mCap != null) {
        "$" + NumberFormat.getNumberInstance().format(_mCap.toLong())
    } else {
        null
    }
    val totalSupplyString: String? = if (_totalSupply != null) {
        NumberFormat.getNumberInstance().format(_totalSupply.toLong())
    } else {
        "∞"
    }
    val circulatingSupplyString: String? = if (_circulatingSupply != null) {
        NumberFormat.getNumberInstance().format(_circulatingSupply.toLong())
    } else {
        null
    }
    val progress24H = if (low24H != null && high24H != null && currentPrice != null) {
        ((currentPrice - low24H) / (high24H - low24H)).toFloat()
    } else {
        0f
    }
    val athDate = _athDate?.substring(0, _athDate.indexOf("T"))?.split("-")?.let {
        "${DateFormatSymbols().months[it[1].toInt().minus(1)]} ${it[2]} ${it[0]}"
    }
    val score = _score?.decimalCount(2)
    val priceChangePercentage = _priceChangePercentage?.decimalCount(2)
    val roiString = if (_roi != null && _roi != 0f) {
        "%" + _roi.toDouble().decimalCount(2)
    } else {
        null
    }


    companion object {
        fun transformDataModelToUiModel(cfd: CoinFullData): CoinInfoUI {
            return CoinInfoUI(
                id = cfd.id,
                symbol = cfd.symbol,
                name = cfd.name,
                image = cfd.image.large,
                mCapRank = cfd.marketCapRank,
                currentPrice = cfd.marketData?.currentPrice?.get("usd"),
                _priceChangePercentage = cfd.marketData?.priceChangePercentage24h,
                _mCap = cfd.marketData?.marketCap?.get("usd"),
                _totalSupply = cfd.marketData?.totalSupply,
                _circulatingSupply = cfd.marketData?.circulatingSupply,
                _roi = (cfd.marketData?.roi?.percentage) ?: 0f,
                _score = cfd.coingeckoScore,
                desc = cfd.description["en"],
                low24H = cfd.marketData?.low24h?.get("usd"),
                high24H = cfd.marketData?.high24h?.get("usd"),
                ath = cfd.marketData?.ath?.get("usd"),
                athPriceChangePercentage = cfd.marketData?.athChangePercentage?.get("usd"),
                _athDate = cfd.marketData?.athDate?.get("usd"),
                communityData = cfd.communityData,
                links = cfd.links
            )
        }

    }
}

