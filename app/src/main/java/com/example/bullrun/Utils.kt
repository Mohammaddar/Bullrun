package com.example.bullrun

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.bullrun.ui.custom.CustomMarker
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.math.BigDecimal
import java.math.RoundingMode

fun setupLineChart(context: Context, chart: LineChart, values: List<Entry>,timeFrame:String) {
    // create marker to display box when values are selected
    val mv = CustomMarker(context)
    mv.timeFrame=timeFrame
    mv.chartView = chart
    chart.marker = mv

    //xAxis.valueFormatter=OneMonthValueFormatter(Calendar.getInstance().get(Calendar.MONTH))
    chart.run {
        xAxis.xOffset = 0f
        xAxis.setLabelCount(6, /*force: */true)
        xAxis.axisMinimum = 1f
        xAxis.setDrawLabels(true)
        xAxis.axisMaximum = values.size.toFloat()
    }

    val set1: LineDataSet
    if (chart.data != null &&
        chart.data.dataSetCount > 0
    ) {
        set1 = chart.data.getDataSetByIndex(0) as LineDataSet
        set1.values = values
        set1.notifyDataSetChanged()
        chart.data.notifyDataChanged()
        chart.notifyDataSetChanged()
    } else {
        set1 = LineDataSet(values, "DataSet 1")
        set1.setDrawIcons(false)
        set1.color = ContextCompat.getColor(requireNotNull(context), R.color.black_light)
        set1.lineWidth = 2f
        set1.setDrawCircles(false)
        set1.setDrawValues(false)
        set1.setDrawHorizontalHighlightIndicator(false)
        set1.setDrawCircleHole(false)
        set1.formLineWidth = 1f
        set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        set1.formSize = 15f
        set1.valueTextSize = 9f
        set1.enableDashedHighlightLine(10f, 5f, 0f)
        set1.disableDashedHighlightLine()
        set1.setDrawVerticalHighlightIndicator(true)
        set1.setDrawFilled(true)
        set1.fillDrawable =
            ContextCompat.getDrawable(context, R.drawable.gradient_chart_fill)
//        set1.fillFormatter =
//            IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.cubicIntensity = 0.2f

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(set1)
        val data = LineData(dataSets)
        chart.data = data
    }
    Log.d("TAGDE","$values")
    chart.invalidate()
}

fun setupLineChart2(context: Context, chart: LineChart, values: List<Entry>, rgb: List<String>) {
    chart.setDrawBorders(false)
    chart.setDrawGridBackground(false)
    chart.description.isEnabled = false
    //chart.isVerticalScrollBarEnabled=false
    chart.isDoubleTapToZoomEnabled = false
//        chart.axisRight.setDrawAxisLine(false)
//        chart.axisRight.setDrawGridLines(false)
//        chart.xAxis.setDrawAxisLine(false)
//        chart.xAxis.setDrawGridLines(false)
    chart.xAxis.isEnabled = false
    chart.xAxis.setDrawGridLines(false)
    chart.xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
    chart.axisLeft.isEnabled = false
    chart.axisRight.isEnabled = false
    chart.legend.isEnabled = false
    chart.setPinchZoom(false)
    chart.setTouchEnabled(true)
    chart.isDragEnabled = true
    chart.setScaleEnabled(false)
    chart.setViewPortOffsets(0f, 0f, 0f, 0f)
    //chart.xAxis.valueFormatter=OneMonthValueFormatter(Calendar.getInstance().get(Calendar.MONTH))
    chart.xAxis.xOffset = 100f
    chart.xAxis.setLabelCount(10, /*force: */true)
    chart.xAxis.axisMinimum = 0f
    chart.xAxis.axisMaximum = 29f
    chart.setTouchEnabled(false)


    val set1: LineDataSet
    if (chart.data != null &&
        chart.data.dataSetCount > 0
    ) {
        set1 = chart.data.getDataSetByIndex(0) as LineDataSet
        set1.values = values
        set1.notifyDataSetChanged()
        chart.data.notifyDataChanged()
        chart.notifyDataSetChanged()
    } else {
        // create a dataset and give it a type
        set1 = LineDataSet(values, "DataSet 1")
        set1.setDrawIcons(false)

        // black lines and points
        set1.color = Color.argb(160, rgb[0].toInt(), rgb[1].toInt(), rgb[2].toInt())
        set1.lineWidth = 1.5f
        set1.setCircleColor(Color.BLACK)
        set1.setDrawCircles(false)
        set1.setDrawValues(false)
        set1.setDrawHorizontalHighlightIndicator(false)
        set1.circleRadius = 3f
        set1.setDrawCircleHole(false)
        set1.formLineWidth = 1f
        set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        set1.formSize = 15f
        set1.valueTextSize = 9f
        set1.enableDashedHighlightLine(10f, 5f, 0f)
        set1.disableDashedHighlightLine()
        set1.setDrawVerticalHighlightIndicator(false)
        set1.setDrawFilled(true)
        set1.fillAlpha = 112
        set1.fillColor = Color.argb(255, rgb[0].toInt(), rgb[1].toInt(), rgb[2].toInt())
        set1.fillFormatter =
            IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.cubicIntensity = 0.2f


        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(set1)
        val data = LineData(dataSets)
        chart.data = data
    }

}

fun Double.decimalCount(decimalCount:Int):Double{
    var value=this.toString()
    if(this.toString().contains("E")){
        value=value.substring(0,value.indexOf("E"))
    }
    return try {
        BigDecimal(value).setScale(decimalCount, RoundingMode.HALF_EVEN).toDouble()
    }catch (e:Exception){
        0.0
    }
}

fun setupLineChartTopCoin(context: Context, chart: LineChart, values: List<Entry>) {

    chart.run {
        xAxis.setDrawLabels(false)
    }

    val set1: LineDataSet
    if (chart.data != null &&
        chart.data.dataSetCount > 0
    ) {
        set1 = chart.data.getDataSetByIndex(0) as LineDataSet
        set1.values = values
        set1.notifyDataSetChanged()
        chart.data.notifyDataChanged()
        chart.notifyDataSetChanged()
    } else {
        set1 = LineDataSet(values, "DataSet 1")
        set1.setDrawIcons(false)
        set1.color = ContextCompat.getColor(context, R.color.black_light)
        set1.lineWidth = 2f
        set1.setDrawCircles(false)
        set1.setDrawValues(false)
        set1.setDrawHorizontalHighlightIndicator(false)
        set1.setDrawCircleHole(false)
        set1.formLineWidth = 1f
        set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        set1.formSize = 15f
        set1.valueTextSize = 9f
        set1.enableDashedHighlightLine(10f, 5f, 0f)
        set1.disableDashedHighlightLine()
        set1.setDrawVerticalHighlightIndicator(false)
        set1.setDrawFilled(false)
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.cubicIntensity = 0.2f

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(set1)
        val data = LineData(dataSets)
        chart.data = data
    }
    Log.d("TAGDE","$values")
    chart.invalidate()
}

