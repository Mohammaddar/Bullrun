package com.example.bullrun.ui.custom

import android.content.Context
import android.widget.TextView
import com.example.bullrun.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import java.util.*

class CustomMarker(context: Context?) : MarkerView(context, R.layout.custom_marker) {

    private val tvContent: TextView = findViewById(R.id.tvContent)
    private val timeFrame="1D"
    lateinit var labels:MutableList<Int>

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry, highlight: Highlight) {

        tvContent.text ="${Utils.formatNumber(e.y, 0, true)}  $labels"
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

    fun setTimeFrame(timeFrame: String){
        when(timeFrame){
            "1D" ->{
                labels= mutableListOf()
                val cal=Calendar.getInstance()
                val hour=cal.get(Calendar.HOUR_OF_DAY)
                repeat(30){
                    labels.add((hour-it)%24)
                }
                for (i in 29 downTo 0){
                    labels.add((hour-i)%24)
                }
            }
            "1W" ->{}
            "1M" ->{}
            "3M" ->{}
            "All" ->{}
        }
    }
}