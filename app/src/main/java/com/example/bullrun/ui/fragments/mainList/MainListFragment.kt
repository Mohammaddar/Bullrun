package com.example.bullrun.ui.fragments.mainList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bullrun.R
import com.example.bullrun.databinding.FragmentMainListBinding
import com.example.bullrun.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainListFragment : Fragment() {

    lateinit var binding: FragmentMainListBinding
    lateinit var viewModel: MainListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

        (activity as MainActivity).viewModel.bottomNavigationState.value="VISIBLE"

        val adapter = MainListAdapter(requireNotNull(context)) {
            findNavController().navigate(
                MainListFragmentDirections.actionMainListFragmentToCoinInfoFragment(
                    it
                )
            )
        }

        binding.recyclerCoins.adapter = adapter
        binding.recyclerCoins.layoutManager = LinearLayoutManager(activity)

        lifecycleScope.launch {
            viewModel.coinsList.collectLatest {
                delay(200)
                adapter.submitData(it)
            }
        }

        binding.btnRefresh.setOnClickListener {
            adapter.refresh()
        }

        binding.btnOpenSearchPage.setOnClickListener {
            findNavController().navigate(R.id.action_mainListFragment_to_searchListFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAGL", "onPause")
    }

}