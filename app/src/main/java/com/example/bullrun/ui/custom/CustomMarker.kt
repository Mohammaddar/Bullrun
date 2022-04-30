package com.example.bullrun.ui.custom

import android.content.Context
import android.widget.TextView
import com.example.bullrun.R
import com.example.bullrun.decimalCount
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import org.joda.time.LocalDateTime

class CustomMarker(context: Context?) : MarkerView(context, R.layout.custom_marker) {

    private val tvDate: TextView = findViewById(R.id.txtDate)
    private val tvPrice: TextView = findViewById(R.id.txtPrice)
    var timeFrame = "1D"


    lateinit var labels: MutableList<Int>

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry, highlight: Highlight) {


        //tvContent.text ="${Utils.formatNumber(e.y, 0, true)}  $labels"
        //tvContent.text = e.y.toString()
        var ldt = LocalDateTime()
        if (ldt.minuteOfHour < 30) {
            ldt = ldt.minusHours(1)
        }
        when (timeFrame) {
            "1D" -> {
                ldt = ldt.minusHours(24 - e.x.toInt())
            }
            "1W" -> {
                ldt = ldt.minusHours(168 - e.x.toInt() * 6)
            }
            "1M" -> {
                ldt = ldt.minusDays(30 - e.x.toInt())
            }
            "3M" -> {
                ldt = ldt.minusDays(90 - e.x.toInt() * 3)
            }
        }
        tvDate.text = "${ldt.monthOfYear}/${ldt.dayOfMonth}  ${ldt.hourOfDay}:30"
        tvPrice.text = "${e.y.toDouble().decimalCount(2)} $"
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat()-50)
    }

//    fun setTimeFrame(timeFrame: String){
//        when(timeFrame){
//            "1D" ->{
//                labels= mutableListOf()
//                val cal=Calendar.getInstance()
//                val hour=cal.get(Calendar.HOUR_OF_DAY)
//                repeat(30){
//                    labels.add((hour-it)%24)
//                }
//                for (i in 29 downTo 0){
//                    labels.add((hour-i)%24)
//                }
//            }
//            "1W" ->{}
//            "1M" ->{}
//            "3M" ->{}
//            "All" ->{}
//        }
//    }
}