package com.example.bullrun.ui.fragments.coinInfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bullrun.R
import com.example.bullrun.data.database.model.Coin
import com.example.bullrun.databinding.FragmentCoinInfoBinding
import com.example.bullrun.setupLineChart
import com.example.bullrun.ui.MainActivity
import com.example.bullrun.ui.fragments.portfolio.PortfolioFragmentDirections
import com.example.bullrun.ui.fragments.portfolio.WalletListAdapter
import com.example.bullrun.ui.model.CoinInfoUI
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry


class CoinInfoFragment : Fragment() {
    lateinit var binding: FragmentCoinInfoBinding
    lateinit var viewModel: CoinInfoViewModel

    private val args: CoinInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(
            this,
            CoinInfoViewModelFactory(application, args.coin.coinId)
        ).get(CoinInfoViewModel::class.java)


        binding = FragmentCoinInfoBinding.inflate(layoutInflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).viewModel.bottomNavigationState.value = "HIDDEN"
        viewModel.coinInfo.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.coinInfo = viewModel.coinInfo.value
                setup24HProgress(it)
                if (it.desc==null || it.desc.length<10) {
                    binding.cardDesc.visibility = View.GONE
                }
            }
        }

        setupStatisticsRecycler()

        setupPagerCommunity()

        setupMainChartLAF(binding.lineChart)

        binding.btnAddTransaction.setOnClickListener {
            val coinInfo = viewModel.getSmallCoinInfo()
            coinInfo?.let {
                navigateToAddTransactionPage(
                    it
                )
            }
        }
        viewModel.timeFrame.value = "1D"

        viewModel.med.observe(viewLifecycleOwner) {
            Log.d("TAGDE", "change")
            viewModel.timeFrame.value?.let { tf ->
                viewModel.chartPricesLiveData.value?.let { dataset ->
                    dataset[tf]?.let { data ->
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
                        viewModel.timeFrame.value = "1D"
                    }
                    R.id.btn_1w -> {
                        Log.d("TAGDE", "tf 1w")
                        viewModel.timeFrame.value = "1W"
                    }
                    R.id.btn_1m -> {
                        Log.d("TAGDE", "tf 1m")
                        viewModel.timeFrame.value = "1M"
                    }
                    R.id.btn_3m -> {
                        Log.d("TAGDE", "tf 3m")
                        viewModel.timeFrame.value = "3M"
                    }
                }
            }
        }
        binding.toggleGroup.check(R.id.btn_1d)

    }

    private fun setup24HProgress(coinInfo:CoinInfoUI) {
        binding.guidelinePriceBarProgress.setGuidelinePercent(coinInfo.progress24H)
    }

    private fun setupStatisticsRecycler() {
        val coinStatisticsAdapter = CoinStatisticsAdapter(
            requireNotNull(context)
        ) {
            //onClick
        }
        binding.recyclerStatistics.apply {
            this.adapter = coinStatisticsAdapter
            val lManager = LinearLayoutManager(activity)
            lManager.orientation = RecyclerView.VERTICAL
            layoutManager = lManager
        }
        viewModel.statistics.observe(viewLifecycleOwner) {
            coinStatisticsAdapter.submitList(it)
        }
    }



    private fun navigateToAddTransactionPage(coin: Coin) {
        findNavController().navigate(
            CoinInfoFragmentDirections.actionCoinInfoFragmentToTransactionFragment(
                coin = coin
            )
        )
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
        setupLineChart(requireNotNull(context), binding.lineChart, values, timeFrame)
    }

    private fun setupPagerCommunity() {
        val coinCommunityAdapter = CoinCommunityAdapter(
            requireNotNull(context)
        ) {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.link))
                startActivity(browserIntent)
            }catch (ex:Exception){
                Toast.makeText(context,"Wrong URL",Toast.LENGTH_SHORT).show()
            }
        }
        binding.pagerCommunity.run {
            (getChildAt(0) as RecyclerView).apply {
                offscreenPageLimit = 1
                val padding = resources.getDimensionPixelOffset(R.dimen.dp22)
                setPadding(padding, 0, padding, 0)
                clipToPadding = false
                //TODO Under
            }
            adapter = coinCommunityAdapter
        }

        viewModel.community.observe(viewLifecycleOwner) {
            coinCommunityAdapter.submitList(it)
        }
    }
}