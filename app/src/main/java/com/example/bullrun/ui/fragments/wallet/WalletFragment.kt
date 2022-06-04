package com.example.bullrun.ui.fragments.wallet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.bullrun.databinding.FragmentWalletBinding
import com.example.bullrun.setupLineChart
import com.example.bullrun.ui.MainActivity
import com.example.bullrun.ui.fragments.home.Last24HPagerAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.google.android.material.tabs.TabLayoutMediator

class WalletFragment : Fragment() {

    lateinit var binding: FragmentWalletBinding
    lateinit var viewModel: WalletViewModel
    private val args: WalletFragmentArgs by navArgs()
    private lateinit var cntxt: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val application = requireNotNull(this.activity).application
        binding = FragmentWalletBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(
            this,
            WalletViewModelFactory(application)
        ).get(WalletViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cntxt = requireNotNull(context)
        (activity as MainActivity).viewModel.bottomNavigationState.value = "HIDDEN"
        viewModel.walletName.value = args.walletName

        //setupRecyclerAssets()
        setupAssetsHTabAndPager()
        setupMainChartLAF(binding.lineChart)


//        lifecycleScope.launch {
//            viewModel.timeFrame.collectLatest {
//                viewModel.chartsDataset[it]?.let { it1 -> setupBarChart(it,it1) }
//            }
//        }

        viewModel.med.observe(viewLifecycleOwner) {
            Log.d("TAGDE", "change")
            viewModel.timeFrame.value?.let { tf ->
                viewModel.chartsDataset.value?.let { dataset ->
                    val data = dataset[tf]
                    if (data != null && data.size != 0) {
                        Log.d("TAGDE", "setupBarChart call $data")
                        setupBarChart(tf, data)
                    }
                }
            }
        }

//        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
//            if (isChecked) {
//                when (checkedId) {
//                    R.id.btn_1d -> {
//                        Log.d("TAGDE", "tf 1d")
//                        viewModel.timeFrame.value = "1D"
//                    }
//                    R.id.btn_1w -> {
//                        Log.d("TAGDE", "tf 1w")
//                        viewModel.timeFrame.value = "1W"
//                    }
//                    R.id.btn_1m -> {
//                        Log.d("TAGDE", "tf 1m")
//                        viewModel.timeFrame.value = "1M"
//                    }
//                    R.id.btn_3m -> {
//                        Log.d("TAGDE", "tf 3m")
//                        viewModel.timeFrame.value = "3M"
//                    }
//                }
//            }
//        }
//        binding.toggleGroup.check(R.id.btn_1d)

        viewModel.timeFrame.observe(viewLifecycleOwner) {
            Log.d("TAGDE", "tf changed $it")
        }
    }

    private fun setupMainChartLAF(chart: LineChart) {
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

//    private fun setupRecyclerAssets() {
//        val assetAdapter = AssetListAdapter(cntxt)
//        binding.recyclerAssets.adapter = assetAdapter
//        binding.recyclerAssets.layoutManager = GridLayoutManager(cntxt, 2)
//
//        viewModel.assetsList.observe(viewLifecycleOwner) {
//            lifecycleScope.launch {
//                val ls = mutableListOf<AssetItem>()
//                val def = lifecycleScope.async(Dispatchers.Default) {
//                    ls.addAll(it)
//                    repeat(0) {
//                        ls.add(
//                            EmptyUI()
//                        )
//                    }
//                    return@async ls
//                }
//                assetAdapter.submitList(def.await())
//            }
//        }
//    }

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
        setupLineChart(cntxt, binding.lineChart, values, timeFrame)
    }

    private fun setupAssetsHTabAndPager() {
        val pagerAdapter = AssetsPagerAdapter(this)
        binding.viewPagerAssets.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayoutAssets, binding.viewPagerAssets) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Holdings"
                }
                1 -> {
                    tab.text = "Transactions"
                }
            }
        }.attach()
    }
}