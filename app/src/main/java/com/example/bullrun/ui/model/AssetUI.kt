package com.example.bullrun.ui.model

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.bumptech.glide.Glide
import com.example.bullrun.R
import com.example.bullrun.data.database.model.Asset
import com.github.mikephil.charting.data.Entry
import com.google.android.material.card.MaterialCardView
import java.math.BigDecimal
import java.math.RoundingMode

sealed class AssetItem
data class AssetUI(
    var coinId: String,
    val coinName: String,
    val imageURL: String? = null,
    val symbol: String,
    val currentPrice: Double,
    var totalBuyingVolume: Double = 0.0,
    var totalSellingVolume: Double = 0.0,
    val totalBuyingCost: Double = 0.0,
    val totalSellingIncome: Double = 0.0,
    val imageBitmap: Bitmap?=null,
    val theme:String,
    val tickers:List<Entry>
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
        fun transformDataModelToUiModel(ls: List<Asset>,context:Context): List<AssetUI> {
            Log.d("TAGP", "thread4 : ${Thread.currentThread().name}")
            return ls.map {
                Log.d("TAGI", "${it.coinName} 1 : ${Thread.currentThread().name}")
                val bitmap = Glide.with(context).asBitmap().load(it.image).submit().get()
                Log.d("TAGI", "${it.coinName} 2 : ${Thread.currentThread().name}")
                val theme=getTheme(bitmap,it.coinName)
                Log.d("TAGI", "${it.coinName} 5 : ${Thread.currentThread().name}")
                val tickers = mutableListOf<Entry>()
                tickers.run {
                    add(Entry(0f, 100f))
                    add(Entry(1f, 104f))
                    add(Entry(2f, 110f))
                    add(Entry(3f, 108f))
                    add(Entry(4f, 124f))
                    add(Entry(5f, 128f))
                    add(Entry(6f, 120f))
                    add(Entry(7f, 118f))
                    add(Entry(8f, 121f))
                    add(Entry(9f, 114f))
                    add(Entry(10f, 112f))
                    add(Entry(11f, 111f))
                    add(Entry(12f, 108f))
                    add(Entry(13f, 112f))
                    add(Entry(14f, 114f))
                    add(Entry(15f, 115f))
                    add(Entry(16f, 111f))
                    add(Entry(17f, 108f))
                    add(Entry(18f, 107f))
                    add(Entry(19f, 108f))
                    add(Entry(20f, 105f))
                    add(Entry(21f, 107f))
                    add(Entry(22f, 106f))
                    add(Entry(23f, 104f))
                    add(Entry(24f, 106f))
                    add(Entry(25f, 107f))
                    add(Entry(26f, 109f))
                    add(Entry(27f, 105f))
                    add(Entry(28f, 104f))
                    add(Entry(29f, 106f))
                }
                AssetUI(
                    it.coinId,
                    it.coinName,
                    it.image,
                    it.symbol,
                    it.currentPrice,
                    it.totalBuyingVolume,
                    it.totalSellingVolume,
                    it.totalBuyingCost,
                    it.totalSellingIncome,
                    bitmap,
                    theme,
                    tickers
                )
            }
        }
        private fun getTheme(bitmap: Bitmap,coinName: String):String {
            Log.d("TAGI", "${coinName} 3 : ${Thread.currentThread().name}")
            var r=0
            var g=0
            var b=0
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
            r/=25
            g/=25
            b/=25
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

    private fun formatDecimal(value: Double, decimalCount: Int): Double {
        return BigDecimal(value).setScale(decimalCount, RoundingMode.HALF_EVEN).toDouble()
    }


}

data class EmptyUI(val name: String = "empty"):AssetItem()
