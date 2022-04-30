package com.example.bullrun.ui.fragments.mainList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bullrun.R
import com.example.bullrun.data.database.model.Coin
import com.example.bullrun.databinding.MainListItemBinding

class MainListAdapter(val context: Context, private val onClick: (coin: Coin) -> Unit) :
    PagingDataAdapter<Coin, MainListVH>(MainListDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListVH {
        return MainListVH.create(parent)
    }

    override fun onBindViewHolder(holder: MainListVH, position: Int) {
        getItem(position)?.let {
            holder.bind(it, context, onClick)
        }
    }
}

class MainListVH private constructor(private val binding: MainListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var onClick: (coin: Coin) -> Unit
    fun bind(coin: Coin, context: Context, onClick: (coin: Coin) -> Unit) {
        this.onClick = onClick
        binding.coin = coin
        binding.context = context
        binding.holder = this
        binding.executePendingBindings()
        coin.priceChangePercentage24h?.let {
            when {
                coin.priceChangePercentage24h > 0 -> binding.txt24hchange.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    )
                )
                coin.priceChangePercentage24h == 0.0 -> binding.txt24hchange.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.gray_500
                    )
                )
                coin.priceChangePercentage24h < 0 -> binding.txt24hchange.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.red
                    )
                )
            }
        }
    }

    fun onClickListener(coin: Coin) = onClick(coin)

    companion object {
        fun create(parent: ViewGroup): MainListVH {
            val binding =
                MainListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MainListVH(binding)
        }
    }
}

class MainListDiffUtil : DiffUtil.ItemCallback<Coin>() {
    override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
        return oldItem.coinName.equals(newItem.coinName)
    }

    override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
        return oldItem == newItem
    }

}
