package com.example.bullrun.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bullrun.databinding.FragmentBasicRecyclerViewBinding

class Last24HPagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return BasicRecyclerViewFragment(position, this.fragment)
    }
}

class BasicRecyclerViewFragment(val position: Int, val fragment: Fragment) : Fragment() {

    lateinit var binding: FragmentBasicRecyclerViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasicRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        when (position) {
            0 -> {
                val topGainersAdapter = TopMoversAdapter(
                    requireNotNull(context)
                ) {
                    //onClick
                }

                binding.recycler.apply {
                    this.adapter = topGainersAdapter
                    val lManager = LinearLayoutManager(activity)
                    lManager.orientation = RecyclerView.VERTICAL
                    layoutManager = lManager
                }

                (fragment as HomeFragment).viewModel.topMovers.observe(viewLifecycleOwner) {
                    topGainersAdapter.submitList(it["Gainers"])
                }
            }
            1 -> {
                val topLosersAdapter = TopMoversAdapter(
                    requireNotNull(context)
                ) {
                    //onClick
                }

                binding.recycler.apply {
                    this.adapter = topLosersAdapter
                    val lManager = LinearLayoutManager(activity)
                    lManager.orientation = RecyclerView.VERTICAL
                    layoutManager = lManager
                }

                (fragment as HomeFragment).viewModel.topMovers.observe(viewLifecycleOwner) {
                    topLosersAdapter.submitList(it["Losers"])
                }
            }
            2 -> {
                val trendingAdapter = TrendingAdapter(
                    requireNotNull(context)
                ) {
                    //onClick
                }

                binding.recycler.apply {
                    this.adapter = trendingAdapter
                    val lManager = LinearLayoutManager(activity)
                    lManager.orientation = RecyclerView.VERTICAL
                    layoutManager = lManager
                }

                (fragment as HomeFragment).viewModel.trending.observe(viewLifecycleOwner) {
                    trendingAdapter.submitList(it)
                }
            }
        }

    }
}