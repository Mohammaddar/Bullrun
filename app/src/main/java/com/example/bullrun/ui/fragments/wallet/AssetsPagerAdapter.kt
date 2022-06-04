package com.example.bullrun.ui.fragments.wallet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bullrun.databinding.BasicRecyclerViewBinding

class AssetsPagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return AssetsPagerBasicRecyclerViewFragment(position, this.fragment)
    }
}

class AssetsPagerBasicRecyclerViewFragment(val position: Int, val fragment: Fragment) : Fragment() {

    lateinit var binding: BasicRecyclerViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BasicRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        when (position) {
            0 -> {
                val holdingsAdapter = HoldingsAdapter(){
                    //TODO
                }
                binding.recycler.apply {
                    this.adapter = holdingsAdapter
                    val lManager = LinearLayoutManager(activity)
                    lManager.orientation = RecyclerView.VERTICAL
                    layoutManager = lManager
                }

                (fragment as WalletFragment).viewModel.holdings.observe(viewLifecycleOwner) {
                    holdingsAdapter.submitList(it)
                }
            }
            1 -> {
                val transactionsAdapter = TransactionsAdapter(){
                    //TODO
                }
                binding.recycler.apply {
                    this.adapter = transactionsAdapter
                    val lManager = LinearLayoutManager(activity)
                    lManager.orientation = RecyclerView.VERTICAL
                    layoutManager = lManager
                }

                (fragment as WalletFragment).viewModel.transactions.observe(viewLifecycleOwner) {
                    Log.d("TAGNMD","$it")
                    transactionsAdapter.submitList(it)
                }
            }
        }

    }
}