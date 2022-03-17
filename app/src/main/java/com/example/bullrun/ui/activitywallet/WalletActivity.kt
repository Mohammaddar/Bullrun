package com.example.bullrun.ui.activitywallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bullrun.R
import com.example.bullrun.databinding.ActivityWalletBinding
import com.example.bullrun.setupLineChart
import com.example.bullrun.ui.fragments.coinInfo.CoinInfoFragmentArgs
import com.example.bullrun.ui.model.AssetItem
import com.example.bullrun.ui.model.EmptyUI
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

        viewModel.walletName.value=args.walletName
        setupRecyclerAssets()
        setupBarChart()

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


    private fun setupBarChart() {
        val values = mutableListOf<Entry>()
        values.run {
            add(Entry(0f, 100f))
            add(Entry(1f, 104f))
            add(Entry(2f, 110f))
            add(Entry(3f, 108f))
            add(Entry(4f, 124f))
            add(Entry(5f, 128f))
            add(Entry(6f, 120f))
            add(Entry(7f, 118f))
            add(Entry(8f, 121f))
            add(Entry(9f, 114f))
            add(Entry(10f, 112f))
            add(Entry(11f, 111f))
            add(Entry(12f, 108f))
            add(Entry(13f, 112f))
            add(Entry(14f, 114f))
            add(Entry(15f, 115f))
            add(Entry(16f, 111f))
            add(Entry(17f, 108f))
            add(Entry(18f, 107f))
            add(Entry(19f, 108f))
            add(Entry(20f, 105f))
            add(Entry(21f, 107f))
            add(Entry(22f, 106f))
            add(Entry(23f, 104f))
            add(Entry(24f, 106f))
            add(Entry(25f, 107f))
            add(Entry(26f, 109f))
            add(Entry(27f, 105f))
            add(Entry(28f, 104f))
            add(Entry(29f, 106f))
        }
        setupLineChart(this, binding.lineChart, values)
    }
}