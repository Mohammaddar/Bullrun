package com.example.bullrun.ui.model

import android.util.Log
import com.example.bullrun.data.database.model.Asset
import java.math.BigDecimal
import java.math.RoundingMode

sealed class AssetItem
data class AssetUI(
    var coinId: String,
    val coinName: String,
    val image: String? = null,
    val symbol: String,
    val currentPrice: Double,
    var totalBuyingVolume: Double = 0.0,
    var totalSellingVolume: Double = 0.0,
    val totalBuyingCost: Double = 0.0,
    val totalSellingIncome: Double = 0.0,
) :AssetItem() {

    private val _totalBuyingVolume = totalBuyingVolume

    private val _totalSellingVolume = totalSellingVolume

    private val _netHoldingVolume: Double = _totalBuyingVolume - _totalSellingVolume
    val netHoldingVolume: Double = formatDecimal(_netHoldingVolume, 7)

    private val _totalHoldingValue = _netHoldingVolume * currentPrice
    val totalHoldingValue: Double = formatDecimal(_totalHoldingValue, 2)

    private val _netCost = totalBuyingCost - totalSellingIncome
    val netCost: Double = formatDecimal(_netCost, 2)

    private val _avgEntry = _netCost / _netHoldingVolume
    val avgEntry: Double = formatDecimal(_avgEntry, 2)

    private val _pnlValue = (currentPrice - _avgEntry) * _netHoldingVolume
    val pnlValue: Double = formatDecimal(_pnlValue, 2)

    private val _pnlPercentage = _pnlValue / totalBuyingCost
    val pnlPercentage: Double = formatDecimal(_pnlPercentage, 2)

    //
    var visibility: Boolean = false


    init {
        totalBuyingVolume = formatDecimal(_totalBuyingVolume, 7)
        totalSellingVolume = formatDecimal(_totalSellingVolume, 7)
    }


    companion object {
        fun transformDataModelToUiModel(ls: List<Asset>): List<AssetUI> {
            Log.d("TAGP", "thread4 : ${Thread.currentThread().name}")
            return ls.map {
                AssetUI(
                    it.coinId,
                    it.coinName,
                    it.image,
                    it.symbol,
                    it.currentPrice,
                    it.totalBuyingVolume,
                    it.totalSellingVolume,
                    it.totalBuyingCost,
                    it.totalSellingIncome
                )
            }
        }
    }

    private fun formatDecimal(value: Double, decimalCount: Int): Double {
        return BigDecimal(value).setScale(decimalCount, RoundingMode.HALF_EVEN).toDouble()
    }
}

data class EmptyUI(val name: String = "empty"):AssetItem()
