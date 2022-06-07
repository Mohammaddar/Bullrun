package com.example.bullrun.ui.fragments.mainList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bullrun.R
import com.example.bullrun.data.database.model.CoinList
import com.example.bullrun.databinding.FragmentMainListBinding
import com.example.bullrun.ui.MainActivity
import com.example.bullrun.ui.fragments.searchList.SearchListAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainListFragment : Fragment() {

    lateinit var binding: FragmentMainListBinding
    lateinit var viewModel: MainListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(
            this,
            MainListViewModelFactory(application)
        ).get(MainListViewModel::class.java)

        binding = FragmentMainListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).viewModel.bottomNavigationState.value = "VISIBLE"

        setupCryptoAssetsTabAndPager()
//        binding.btnRefresh.setOnClickListener {
//            adapter.refresh()
//        }

        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_mainListFragment_to_searchListFragment)
        }


    }

    override fun onPause() {
        super.onPause()
        Log.d("TAGL", "onPause")
    }

    private fun setupCryptoAssetsTabAndPager() {
        val pagerAdapter = CryptoAssetsPagerAdapter(this)
        binding.viewPagerCryptoAssets.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayoutCryptoAssets, binding.viewPagerCryptoAssets) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "All Coins"
                }
                1 -> {
                    tab.text = "Watchlist"
                }
            }
        }.attach()

//        val adapter = SearchListAdapter()
//        binding.recyclerMock.apply {
//            this.adapter = adapter
//            val lManager = LinearLayoutManager(activity)
//            lManager.orientation = RecyclerView.VERTICAL
//            layoutManager = lManager
//        }
//
//        adapter.submitList(
//            listOf(
//                CoinList(coinId = "id"),
//                CoinList(coinId = "id1"),
//                CoinList(coinId = "id2"),
//                CoinList(coinId = "id3"),
//                CoinList(coinId = "id4"),
//                CoinList(coinId = "id5"),
//                CoinList(coinId = "id6"),
//                CoinList(coinId = "id7"),
//                CoinList(coinId = "id8"),
//                CoinList(coinId = "id9"),
//                CoinList(coinId = "id10"),
//                CoinList(coinId = "id11")
//            )
//        )
    }

}