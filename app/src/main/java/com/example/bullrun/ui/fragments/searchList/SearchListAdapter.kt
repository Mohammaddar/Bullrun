package com.example.bullrun.ui.fragments.searchList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bullrun.databinding.SearchListItemBinding
import com.example.bullrun.data.database.model.CoinList

class SearchListAdapter : ListAdapter<CoinList, SearchListVH>(SearchListDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListVH {
        return SearchListVH.create(parent)
    }

    override fun onBindViewHolder(holder: SearchListVH, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class SearchListVH private constructor(private val binding: SearchListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(coinList: CoinList){
        binding.coinList=coinList
    }

    companion object{
        fun create(parent: ViewGroup): SearchListVH {
            val binding = SearchListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SearchListVH(binding)
        }
    }
}

class SearchListDiffUtil : DiffUtil.ItemCallback<CoinList>() {
    override fun areItemsTheSame(oldItem: CoinList, newItem: CoinList): Boolean {
        return oldItem.coinName.equals(newItem.coinName)
    }

    override fun areContentsTheSame(oldItem: CoinList, newItem: CoinList): Boolean {
        return oldItem == newItem
    }

}