package com.example.bullrun.ui.fragments.coinInfo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bullrun.databinding.StatisticsListItemBinding
import com.example.bullrun.ui.model.CoinCommunityUi
import com.example.bullrun.ui.model.CoinStatisticUi

class CoinStatisticsAdapter(val context: Context, private val onClick: (coinStatistic: CoinStatisticUi) -> Unit) :
    ListAdapter<CoinStatisticUi, CoinStatisticVH>(CoinStatisticsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinStatisticVH {
        return CoinStatisticVH.create(parent)
    }

    override fun onBindViewHolder(holder: CoinStatisticVH, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onClick)
        }
    }
}

class CoinStatisticVH private constructor(private val binding: StatisticsListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var onClick: (coinStatistic: CoinStatisticUi) -> Unit
    fun bind(coinStatistic: CoinStatisticUi, onClick: (coinStatistic: CoinStatisticUi) -> Unit) {
        this.onClick = onClick
        binding.coinStatistic=coinStatistic
    }

    fun onClickListener(coinStatistic: CoinStatisticUi) = onClick(coinStatistic)

    companion object {
        fun create(parent: ViewGroup): CoinStatisticVH {
            val binding =
                StatisticsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CoinStatisticVH(binding)
        }
    }
}

class CoinStatisticsDiffUtil : DiffUtil.ItemCallback<CoinStatisticUi>() {
    override fun areItemsTheSame(oldItem: CoinStatisticUi, newItem: CoinStatisticUi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CoinStatisticUi, newItem: CoinStatisticUi): Boolean {
        return oldItem == newItem
    }

}
