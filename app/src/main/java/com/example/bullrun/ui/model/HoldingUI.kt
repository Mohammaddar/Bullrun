package com.example.bullrun.ui.model

import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.example.bullrun.data.database.model.Asset
import com.example.bullrun.decimalCount

sealed class HoldingItem
data class HoldingUI(
    var coinId: String,
    val coinName: String,
    val imageURL: String? = null,
    val symbol: String,
    val currentPrice: Double,
    var totalBuyingVolume: Double = 0.0,
    var totalSellingVolume: Double = 0.0,
    val totalBuyingCost: Double = 0.0,
    val totalSellingIncome: Double = 0.0,
    val walletName:String
) : HoldingItem() {

    private val _totalBuyingVolume = totalBuyingVolume

    private val _totalSellingVolume = totalSellingVolume

    private val _netHoldingVolume: Double = _totalBuyingVolume - _totalSellingVolume
    val netHoldingVolume: Double = _netHoldingVolume.decimalCount(7)

    private val _totalHoldingValue = _netHoldingVolume * currentPrice
    val totalHoldingValue: Double = _totalHoldingValue.decimalCount(2)

    private val _netCost = totalBuyingCost - totalSellingIncome
    val netCost: Double = _netCost.decimalCount(2)

    private val _avgEntry = _netCost / _netHoldingVolume
    val avgEntry: Double = _avgEntry.decimalCount(2)

    private val _pnlValue = (currentPrice - _avgEntry) * _netHoldingVolume
    val pnlValue: Double = _pnlValue.decimalCount(2)

    private val _pnlPercentage = (_pnlValue / totalBuyingCost) * 100
    val pnlPercentage: Double = _pnlPercentage.decimalCount(2)

    //
    var visibility: Boolean = false


    init {
        totalBuyingVolume = _totalBuyingVolume.decimalCount(7)
        totalSellingVolume = _totalSellingVolume.decimalCount(7)
    }


    companion object {
        fun transformDataModelToUiModel(ls: List<Asset>): List<HoldingUI> {
            Log.d("TAGP", "thread4 : ${Thread.currentThread().name}")
            return ls.map {
                Log.d("TAGI", "${it.coinName} 1 : ${Thread.currentThread().name}")
                HoldingUI(
                    it.coinId,
                    it.coinName,
                    it.image,
                    it.symbol,
                    it.currentPrice,
                    it.totalBuyingVolume,
                    it.totalSellingVolume,
                    it.totalBuyingCost,
                    it.totalSellingIncome,
                    it.walletName
                )
            }
        }

        private fun getTheme(bitmap: Bitmap, coinName: String): String {
            Log.d("TAGI", "${coinName} 3 : ${Thread.currentThread().name}")
            var r = 0
            var g = 0
            var b = 0
            val width = bitmap.width
            val height = bitmap.height
            var pixel = 0
            for (i in 1..5) {
                for (j in 1..5) {
                    pixel = bitmap.getPixel((width / 6) * i, (height / 6) * j)
                    r += pixel.red
                    g += pixel.green
                    b += pixel.blue
                }
            }
            r /= 25
            g /= 25
            b /= 25
//            Log.d("TAGCO", "${coinName} r:$r , g=$g , b=$b")
//            val colors = listOf(
//                Triple(222, 226, 230),
//                Triple(183, 228, 199),
//                Triple(255, 215, 186),
//                Triple(253, 255, 182),
//                Triple(202, 240, 248)
//            )
//
//            var minDiff = 2000
//            var index = 0
//            for (i in colors.indices) {
//                val diff = kotlin.math.abs(r - colors[i].toList()[0]) +
//                        kotlin.math.abs(g - colors[i].toList()[1]) +
//                        kotlin.math.abs(b - colors[i].toList()[2])
//                Log.d("TAGCO", "${coinName} diff:$diff")
//                if (diff < minDiff) {
//                    minDiff = diff
//                    index = i
//                }
//            }
//            Log.d("TAGI", "${coinName} 4 : ${Thread.currentThread().name}")
            return "$r,$g,$b"
        }
    }


}

data class EmptyUI(val name: String = "empty") : HoldingItem()
