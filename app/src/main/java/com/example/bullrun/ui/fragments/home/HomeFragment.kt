package com.example.bullrun.ui.fragments.home

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bullrun.databinding.FragmentHomeBinding
import com.example.bullrun.decimalCount
import com.example.bullrun.setupLineChartTopCoin
import com.example.bullrun.ui.MainActivity
import com.example.bullrun.ui.MainActivityViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    private var displayWidth: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(application)
        ).get(HomeViewModel::class.java)

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        (activity as MainActivity).viewModel.bottomNavigationState.value="VISIBLE"

        startShimmer(binding.shimmerTopGainers, binding.recyclerTopGainers)
        startShimmer(binding.shimmerTopLosers, binding.recyclerTopLosers)

        displayWidth = Resources.getSystem().displayMetrics.widthPixels

        setupTopMoversRecyclers()
        setupTrendingRecycler()
        setupTopCoinChart()
        setupGlobalData()
    }

    private fun setupGlobalData() {
        viewModel.globalData.observe(viewLifecycleOwner) {
            binding.globalDataUi=it
            Log.d("TAGGL","${it.totalMarketCap?.decimalCount(2)}")
        }
    }

    private fun startShimmer(shimmer: ShimmerFrameLayout, view: RecyclerView) {
        view.visibility = View.INVISIBLE
        shimmer.visibility = View.VISIBLE
        shimmer.startShimmer()
    }

    private fun stopShimmer(shimmer: ShimmerFrameLayout, view: RecyclerView) {
        view.visibility = View.VISIBLE
        shimmer.visibility = View.INVISIBLE
        shimmer.stopShimmer()
    }

    private fun setupTopMoversRecyclers() {
        val topGainersAdapter = TopMoversAdapter(
            requireNotNull(context),
            (displayWidth * 0.4).toInt()
        ) {
            //onClick
        }
        binding.recyclerTopGainers.apply {
            this.adapter = topGainersAdapter
            val lManager = LinearLayoutManager(activity)
            lManager.orientation = RecyclerView.HORIZONTAL
            layoutManager = lManager
        }

        val topLosersAdapter = TopMoversAdapter(
            requireNotNull(context),
            (displayWidth * 0.4).toInt()
        ) {
            //onClick
        }
        binding.recyclerTopLosers.apply {
            this.adapter = topLosersAdapter
            val lManager = LinearLayoutManager(activity)
            lManager.orientation = RecyclerView.HORIZONTAL
            layoutManager = lManager
        }

        viewModel.topMovers.observe(viewLifecycleOwner) {
            topGainersAdapter.submitList(it["Gainers"])
            stopShimmer(binding.shimmerTopGainers, binding.recyclerTopGainers)
            topLosersAdapter.submitList(it["Losers"])
            stopShimmer(binding.shimmerTopLosers, binding.recyclerTopLosers)
        }
    }

    private fun setupTrendingRecycler() {
        val trendingAdapter = TrendingAdapter(
            requireNotNull(context),
            (displayWidth * 0.4).toInt()
        ) {
            //onClick
        }
        binding.recyclerTrending.apply {
            this.adapter = trendingAdapter
            val lManager = LinearLayoutManager(activity)
            lManager.orientation = RecyclerView.HORIZONTAL
            layoutManager = lManager
        }


        viewModel.trending.observe(viewLifecycleOwner) {
            trendingAdapter.submitList(it)
            stopShimmer(binding.shimmerTrending, binding.recyclerTrending)
        }
    }

    fun setupTopTenCoins(){
        viewModel.topTenCoinsList.observe(viewLifecycleOwner){
            //TODO
        }
    }

    private fun setupTopCoinChart() {
        Log.d("TAGDE", "setupBarChart start")

        //setupTopCoinChartLookAndFeel
        binding.lineChartTopCoin.run {
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

        val values = mutableListOf<Entry>()
        viewModel.topCoinPrices1D.observe(viewLifecycleOwner) { ls ->
            values.clear()
            repeat(30) {
                values.add(Entry((it + 1).toFloat(), ls[ls.size-32+it][1].toFloat()))
            }
            Log.d("TAGDE", "setupBarChart finish")
            setupLineChartTopCoin(requireNotNull(context), binding.lineChartTopCoin, values)
        }

    }


}