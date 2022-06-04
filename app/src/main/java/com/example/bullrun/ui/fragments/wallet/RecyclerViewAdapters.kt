package com.example.bullrun.ui.fragments.wallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bullrun.databinding.HoldingItemBinding
import com.example.bullrun.databinding.TransactionItemBinding
import com.example.bullrun.ui.model.HoldingUI
import com.example.bullrun.ui.model.TransactionUI

class HoldingsAdapter(private val onClick: (holding: HoldingUI) -> Unit) :
    ListAdapter<HoldingUI, HoldingVH>(HoldingListDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingVH {
        return HoldingVH.create(parent)
    }

    override fun onBindViewHolder(holder: HoldingVH, position: Int) {
        getItem(position)?.let { holder.bind(it, onClick) }
    }
}

class HoldingVH private constructor(private val binding: HoldingItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var onClick: (holding: HoldingUI) -> Unit
    lateinit var holding: HoldingUI

    fun bind(holding: HoldingUI, onClick: (holding: HoldingUI) -> Unit) {
        this.onClick = onClick
        this.holding = holding
        binding.holder = this
    }

    companion object {
        fun create(parent: ViewGroup): HoldingVH {
            val binding =
                HoldingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return HoldingVH(binding)
        }
    }
}

class HoldingListDiffUtil : DiffUtil.ItemCallback<HoldingUI>() {
    override fun areItemsTheSame(oldItem: HoldingUI, newItem: HoldingUI): Boolean {
        return oldItem.coinName == newItem.coinName
    }

    override fun areContentsTheSame(oldItem: HoldingUI, newItem: HoldingUI): Boolean {
        return oldItem == newItem
    }

}

///////////////////

class TransactionsAdapter(private val onClick: (transaction: TransactionUI) -> Unit) :
    ListAdapter<TransactionUI, TransactionVH>(TransactionListDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionVH {
        return TransactionVH.create(parent)
    }

    override fun onBindViewHolder(holder: TransactionVH, position: Int) {
        getItem(position)?.let { holder.bind(it,onClick) }
    }
}

class TransactionVH private constructor(private val binding: TransactionItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var onClick: (transaction: TransactionUI) -> Unit
    lateinit var transaction:TransactionUI

    fun bind(transaction: TransactionUI,onClick: (transaction: TransactionUI) -> Unit) {
        this.onClick=onClick
        this.transaction=transaction
        binding.holder=this
    }

    fun onClickListener(transaction: TransactionUI)=onClick(transaction)

    companion object {
        fun create(parent: ViewGroup): TransactionVH {
            val binding =
                TransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TransactionVH(binding)
        }
    }
}

class TransactionListDiffUtil : DiffUtil.ItemCallback<TransactionUI>() {
    override fun areItemsTheSame(oldItem: TransactionUI, newItem: TransactionUI): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: TransactionUI, newItem: TransactionUI): Boolean {
        return oldItem == newItem
    }

}