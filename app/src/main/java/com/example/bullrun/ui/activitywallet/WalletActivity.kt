package com.example.bullrun.ui.activitywallet

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bullrun.R
import com.example.bullrun.databinding.ActivityWalletBinding
import com.example.bullrun.setupLineChart
import com.example.bullrun.ui.model.AssetItem
import com.example.bullrun.ui.model.EmptyUI
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class WalletActivity : AppCompatActivity() {

    lateinit var binding: ActivityWalletBinding
    lateinit var viewModel: WalletViewModel
    private val args: WalletActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_wallet
        )

        val application = application
        viewModel = ViewModelProvider(
            this,
            WalletActivityViewModelFactory(application)
        ).get(WalletViewModel::class.java)

        viewModel.walletName.value = args.walletName
        setupRecyclerAssets()

        setupMainChartLAF(binding.lineChart)


//        lifecycleScope.launch {
//            viewModel.timeFrame.collectLatest {
//                viewModel.chartsDataset[it]?.let { it1 -> setupBarChart(it,it1) }
//            }
//        }

        viewModel.med.observe(this) {
            Log.d("TAGDE", "change")
            viewModel.timeFrame.value?.let { tf ->
                viewModel.chartsDataset.value?.let { dataset ->
                    val data=dataset[tf]
                    if (data!=null && data.size!=0){
                        Log.d("TAGDE", "setupBarChart call $data")
                        setupBarChart(tf, data)
                    }
                }
            }
        }

        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_1d -> {
                        Log.d("TAGDE", "tf 1d")
                        viewModel.timeFrame.value="1D"
                    }
                    R.id.btn_1w -> {
                        Log.d("TAGDE", "tf 1w")
                        viewModel.timeFrame.value="1W"
                    }
                    R.id.btn_1m -> {
                        Log.d("TAGDE", "tf 1m")
                        viewModel.timeFrame.value="1M"
                    }
                    R.id.btn_3m -> {
                        Log.d("TAGDE", "tf 3m")
                        viewModel.timeFrame.value="3M"
                    }
                }
            }
        }
        binding.toggleGroup.check(R.id.btn_1d)

        viewModel.timeFrame.observe(this){
            Log.d("TAGDE","tf changed $it")
        }

    }

    private fun setupMainChartLAF(chart:LineChart) {
        //setupMainChartLookAndFeel
        chart.run {
            setDrawBorders(false)
            setDrawGridBackground(false)
            description.isEnabled = false
            isDoubleTapToZoomEnabled = false
            xAxis.isEnabled = true
            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            legend.isEnabled = false
            setPinchZoom(false)
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(false)
            setViewPortOffsets(0f, 0f, 0f, 0f)
        }
    }


    private fun setupRecyclerAssets() {
        val assetAdapter = AssetListAdapter(this)
        binding.recyclerAssets.adapter = assetAdapter
        binding.recyclerAssets.layoutManager = GridLayoutManager(this, 2)

        viewModel.assetsList.observe(this) { it ->
            lifecycleScope.launch {
                val ls = mutableListOf<AssetItem>()
                val def = lifecycleScope.async(Dispatchers.Default) {
                    ls.addAll(it)
                    repeat(0) { _ ->
                        ls.add(
                            EmptyUI()
                        )
                    }
                    return@async ls
                }
                assetAdapter.submitList(def.await())
            }
        }
    }


    private fun setupBarChart(timeFrame: String, prices: List<Double>) {
        Log.d("TAGDE", "setupBarChart start")
        val values = mutableListOf<Entry>()
        when (timeFrame) {
            "1D" -> {
                repeat(24) {
                    values.add(Entry((it + 1).toFloat(), prices[prices.size - it - 1].toFloat()))
                }
            }
            "1W" -> {
                repeat(28) {
                    values.add(Entry((it + 1).toFloat(), prices[prices.size - it - 1].toFloat()))
                }
            }
            "1M" -> {
                repeat(30) {
                    values.add(Entry((it + 1).toFloat(), prices[prices.size - it - 1].toFloat()))
                }
            }
            "3M" -> {
                repeat(30) {
                    values.add(Entry((it + 1).toFloat(), prices[prices.size - it - 1].toFloat()))
                }
            }
        }
        Log.d("TAGDE", "setupBarChart finish")
        setupLineChart(this, binding.lineChart, values,timeFrame)
    }
}