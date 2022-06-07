package com.example.bullrun.ui.fragments.mainList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bullrun.databinding.BasicRecyclerViewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CryptoAssetsPagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AllCoinsFragment()
            }
            1 -> {
                WatchListFragment()
            }
            else -> {
                WatchListFragment()
            }
        }
    }
}

class AllCoinsFragment() : Fragment() {

    lateinit var binding: BasicRecyclerViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("TAGLC", "onCreateView ${this.javaClass.name}")
        binding = BasicRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val adapter = MainListAdapter(requireNotNull(context)) {
            findNavController().navigate(
                MainListFragmentDirections.actionMainListFragmentToCoinInfoFragment(
                    it
                )
            )
        }
        binding.recycler.apply {
            this.adapter = adapter
            val lManager = LinearLayoutManager(activity)
            lManager.orientation = RecyclerView.VERTICAL
            layoutManager = lManager
        }

        lifecycleScope.launch {
            (requireParentFragment() as MainListFragment).viewModel.coinsList.collectLatest {
                delay(200)
                adapter.submitData(it)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAGLC", "onDestroy ${this.javaClass.name}")
    }
}

class RecyclerViewDisabler : OnItemTouchListener {
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return true
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}

class WatchListFragment() : Fragment() {

    lateinit var binding: BasicRecyclerViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("TAGLC", "onCreateView ${this.javaClass.name}")
        binding = BasicRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAGLC", "onDestroy ${this.javaClass.name}")
    }
}