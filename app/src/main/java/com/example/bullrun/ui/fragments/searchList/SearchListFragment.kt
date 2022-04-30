package com.example.bullrun.ui.fragments.searchList

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bullrun.databinding.FragmentSearchListBinding
import com.example.bullrun.ui.MainActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchListFragment : Fragment() {

    private lateinit var binding: FragmentSearchListBinding
    private lateinit var viewModel: SearchListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(
            this,
            SearchListViewModelFactory(application)
        ).get(SearchListViewModel::class.java)

        binding = FragmentSearchListBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).viewModel.bottomNavigationState.value="VISIBLE"

        val adapter = SearchListAdapter()
        binding.recyclerSearch.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(this.context,
                (layoutManager as LinearLayoutManager).orientation
            ))
        }


        var job: Job? = null
        binding.edtSearch.doOnTextChanged { text, _, _, count ->
            if (count > 2) {
                if (job != null && job!!.isActive) {
                    job!!.cancel(null)
                    Log.d("TAG", "cancelled")
                }
                job = lifecycleScope.launch {
                    delay(800)
                    viewModel.getSearchItems(query = text.toString())
                    Log.d("TAG", "done")

                    job!!.join()
                    Log.d("TAG", "is job active ${job!!.isActive}")
                }
            }
            Log.d("TAG", "text $text")
            Log.d("TAG", "count $count")
        }

        viewModel.searchItems.observe(viewLifecycleOwner) {
            Log.d("TAG", "${it.count()}")
            adapter.submitList(it)
        }

    }

}