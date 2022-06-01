package com.example.bullrun.ui.fragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bullrun.databinding.TrendingCoinItemBinding
import com.example.bullrun.ui.model.TrendingCoin

class TrendingAdapter(val context: Context,private val onClick: (trendingCoin: TrendingCoin) -> Unit) :
    ListAdapter<TrendingCoin, TrendingCoinVH>(TrendingCoinDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingCoinVH {
        return TrendingCoinVH.create(parent)
    }

    override fun onBindViewHolder(holder: TrendingCoinVH, position: Int) {
        getItem(position)?.let { holder.bind(it,context,position,onClick) }
    }
}

class TrendingCoinVH private constructor(private val binding: TrendingCoinItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var onClick: (trendingCoin: TrendingCoin) -> Unit
    fun bind(trendingCoin: TrendingCoin, context: Context ,position: Int,onClick: (trendingCoin: TrendingCoin) -> Unit) {
        binding.trendingCoin=trendingCoin
    }

    fun onClickListener(trendingCoin: TrendingCoin) = onClick(trendingCoin)

    companion object {
        fun create(parent: ViewGroup): TrendingCoinVH {
            val binding =
                TrendingCoinItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TrendingCoinVH(binding)
        }
    }
}

class TrendingCoinDiffUtil : DiffUtil.ItemCallback<TrendingCoin>() {
    override fun areItemsTheSame(oldItem: TrendingCoin, newItem: TrendingCoin): Boolean {
        return oldItem.coinName.equals(newItem.coinName)
    }

    override fun areContentsTheSame(oldItem: TrendingCoin, newItem: TrendingCoin): Boolean {
        return oldItem == newItem
    }

}
