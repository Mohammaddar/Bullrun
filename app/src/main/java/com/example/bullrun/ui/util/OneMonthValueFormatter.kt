package com.example.bullrun.ui.util

import android.util.Log
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class OneMonthValueFormatter(private val month:Int) : IndexAxisValueFormatter() {

    val months= listOf<String>("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")
    override fun getFormattedValue(value: Float): String? {
        Log.d("TAGC",value.toString())
        return when(value.toInt()){
            4 -> "4"
            8 -> "8"
            12 -> "12"
            16 -> "16"
            20 -> "20"
            24 -> "24"
            28 -> "28"
            else -> ""
        }
    }
}