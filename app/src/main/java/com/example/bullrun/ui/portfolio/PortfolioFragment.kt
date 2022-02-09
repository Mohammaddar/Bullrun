package com.example.bullrun.ui.portfolio

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bullrun.databinding.FragmentPortfolioBinding
import com.example.bullrun.ui.model.AssetItem
import com.example.bullrun.ui.model.EmptyUI
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PortfolioFragment : Fragment() {

    lateinit var binding: FragmentPortfolioBinding
    lateinit var viewModel: PortfolioViewModel

    init {
        Log.d("TAGL", "Portfolio")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(
            this,
            PortfolioViewModelFactory(application)
        ).get(PortfolioViewModel::class.java)

        binding = FragmentPortfolioBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AssetListAdapter(requireNotNull(context))
        binding.recyclerAssets.adapter = adapter
        binding.recyclerAssets.layoutManager = LinearLayoutManager(activity)

        viewModel.assetsList.observe(viewLifecycleOwner) { it ->
            lifecycleScope.launch {
                val ls = mutableListOf<AssetItem>()
                val def = lifecycleScope.async(Dispatchers.Default) {
                    ls.addAll(it)
                    repeat(6) { _ ->
                        ls.add(
                            EmptyUI()
                        )
                    }
                    return@async ls
                }
                adapter.submitList(def.await())
            }
        }


//        adapter.submitList(
//            listOf(
//                AssetUI(
//                    "btc",
//                    "Bitcoin0",
//                    "",
//                    "",
//                    1522.21,
//                    2.2,
//                    12.5,
//                    4863.5,
//                    4832.6
//                ),
//                AssetUI(
//                    "btc",
//                    "vis",
//                    "",
//                    "",
//                    1522.21,
//                    2.2,
//                    12.5,
//                    4863.5,
//                    4832.6
//                ),
//            )
//        )


        val listener = AppBarLayout.OnOffsetChangedListener { unused, verticalOffset ->
            val seekPosition = -verticalOffset / binding.appbarLayout.totalScrollRange.toFloat()
            binding.motionLayout.progress = seekPosition
        }

        binding.appbarLayout.addOnOffsetChangedListener(listener)
    }
}

