package com.example.bullrun.ui.fragments.home

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.bullrun.databinding.FragmentHomeBinding
import com.example.bullrun.decimalCount
import com.example.bullrun.setupLineChartTopCoin
import com.example.bullrun.ui.MainActivity
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    private var displayWidth: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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

        (activity as MainActivity).viewModel.bottomNavigationState.value = "VISIBLE"

        //startShimmer(binding.shimmerTopGainers, binding.recyclerTopGainers)
        //startShimmer(binding.shimmerTopLosers, binding.recyclerTopLosers)

        displayWidth = Resources.getSystem().displayMetrics.widthPixels

        setupTopCoinChart()
        setupGlobalData()
        setupTopCoinsViewLAF()

        val pagerAdapter=Last24HPagerAdapter(this)
        binding.viewPagerLst24h.adapter=pagerAdapter

        TabLayoutMediator(binding.tabLayoutLst24h, binding.viewPagerLst24h) { tab, position ->
            when (position) {
                0 -> {
                    tab.text="Top Gainers"
                }
                1 -> {
                    tab.text="Top Losers"
                }
                2 -> {
                    tab.text="Trending"
                }
            }
        }.attach()

//        val topGainersAdapter = TopMoversAdapter(
//            requireNotNull(context),
//            (displayWidth * 0.4).toInt()
//        ) {
//            //onClick
//        }
//
//        val topLosersAdapter = TopMoversAdapter(
//            requireNotNull(context),
//            (displayWidth * 0.4).toInt()
//        ) {
//            //onClick
//        }
//
//        val trendingAdapter = TrendingAdapter(
//            requireNotNull(context),
//            (displayWidth * 0.4).toInt()
//        ) {
//            //onClick
//        }
//
//        binding.recyclerLast24h.apply {
//            this.adapter = topGainersAdapter
//            val lManager = LinearLayoutManager(activity)
//            lManager.orientation = RecyclerView.VERTICAL
//            layoutManager = lManager
//        }
//
//        viewModel.topMovers.observe(viewLifecycleOwner) {
//            topGainersAdapter.submitList(it["Gainers"])
//            //stopShimmer(binding.shimmerTopGainers, binding.recyclerTopGainers)
//            topLosersAdapter.submitList(it["Losers"])
//            //stopShimmer(binding.shimmerTopLosers, binding.recyclerTopLosers)
//        }
//
//        viewModel.trending.observe(viewLifecycleOwner) {
//            trendingAdapter.submitList(it)
//            //stopShimmer(binding.shimmerTrending, binding.recyclerTrending)
//        }


    }

    private fun setupTopCoinsViewLAF() {
        binding.card1.doOnLayout {
            val large = (it.measuredWidth * 0.9).toInt()
            binding.card1.layoutParams.height = large
            binding.card2.layoutParams.height = large
            binding.card5.layoutParams.height = large
            binding.card8.layoutParams.height = large
        }
        binding.card2.doOnLayout {
            val small = (it.measuredWidth * 0.9).toInt()
            binding.card3.layoutParams.height = small
            binding.card4.layoutParams.height = small
            binding.card6.layoutParams.height = small
            binding.card7.layoutParams.height = small
            binding.card9.layoutParams.height = small
            binding.card10.layoutParams.height = small
        }
        Log.d("TAGDD", "${binding.card1.layoutParams.width}")
        Log.d("TAGDD", "${binding.card1.width}")


    }

    private fun setupGlobalData() {
        viewModel.globalData.observe(viewLifecycleOwner) {
            it?.let {
                binding.globalDataUi = it
            }
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


    private fun setupTopCoinChart() {
        Log.d("TAGDE", "setupBarChart start")

        //setupTopCoinChartLookAndFeel
        binding.lineChartTopCoin1.run {
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

        binding.lineChartTopCoin8.run {
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
            ls?.let {
                values.clear()
                repeat(30) {
                    values.add(Entry((it + 1).toFloat(), ls[ls.size - 32 + it][1].toFloat()))
                }
                setupLineChartTopCoin(requireNotNull(context), binding.lineChartTopCoin1, values)
                setupLineChartTopCoin(requireNotNull(context), binding.lineChartTopCoin8, values)
            }
        }

    }


}