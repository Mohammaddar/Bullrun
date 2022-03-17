package com.example.bullrun.ui.fragments.portfolio

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bullrun.databinding.WalletListItemBinding
import com.example.bullrun.ui.model.WalletUI

class WalletListAdapter(val context: Context, private val onClick: (wallet: WalletUI) -> Unit) :
    ListAdapter<WalletUI, WalletVH>(WalletListDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletVH {
        return WalletVH.create(parent)

    }


    override fun onBindViewHolder(holder: WalletVH, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onClick)
        }
    }
}

class WalletVH private constructor(val binding: WalletListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var onClick: (wallet: WalletUI) -> Unit
    fun bind(wallet: WalletUI, onClick: (wallet: WalletUI) -> Unit) {
        this.onClick=onClick
        binding.wallet = wallet
        binding.holder=this
    }

    fun onClickListener(wallet: WalletUI) = onClick(wallet)

    companion object {
        fun create(parent: ViewGroup): WalletVH {
            val binding =
                WalletListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return WalletVH(binding)
        }
    }
}


class WalletListDiffUtil : DiffUtil.ItemCallback<WalletUI>() {
    override fun areItemsTheSame(oldItem: WalletUI, newItem: WalletUI): Boolean {
        return oldItem.walletName == newItem.walletName
    }

    override fun areContentsTheSame(oldItem: WalletUI, newItem: WalletUI): Boolean {
        return oldItem == newItem
    }


}
