package com.example.bullrun.ui.fragments.coinInfo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bullrun.databinding.CommunityListItemBinding
import com.example.bullrun.databinding.StatisticsListItemBinding
import com.example.bullrun.ui.model.CoinCommunityUi
import com.example.bullrun.ui.model.CoinStatisticUi

class CoinCommunityAdapter(val context: Context, private val onClick: (coinCommunity: CoinCommunityUi) -> Unit) :
    ListAdapter<CoinCommunityUi, CoinCommunityVH>(CoinCommunityDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinCommunityVH {
        return CoinCommunityVH.create(parent)
    }

    override fun onBindViewHolder(holder: CoinCommunityVH, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onClick)
        }
    }
}

class CoinCommunityVH private constructor(private val binding: CommunityListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var onClick: (coinCommunity: CoinCommunityUi) -> Unit
    fun bind(coinCommunity: CoinCommunityUi, onClick: (coinCommunity: CoinCommunityUi) -> Unit) {
        this.onClick = onClick
        binding.holder=this
        binding.coinCommunity=coinCommunity
    }

    fun onClickListener(coinCommunity: CoinCommunityUi) = onClick(coinCommunity)

    companion object {
        fun create(parent: ViewGroup): CoinCommunityVH {
            val binding =
                CommunityListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CoinCommunityVH(binding)
        }
    }
}

class CoinCommunityDiffUtil : DiffUtil.ItemCallback<CoinCommunityUi>() {
    override fun areItemsTheSame(oldItem: CoinCommunityUi, newItem: CoinCommunityUi): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: CoinCommunityUi, newItem: CoinCommunityUi): Boolean {
        return oldItem == newItem
    }

}