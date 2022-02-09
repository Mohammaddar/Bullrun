package com.example.bullrun.ui.portfolio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bullrun.databinding.AssetListItemBinding
import com.example.bullrun.databinding.AssetListItemEmptyBinding
import com.example.bullrun.ui.model.AssetItem
import com.example.bullrun.ui.model.AssetUI
import com.example.bullrun.ui.model.EmptyUI

class AssetListAdapter(val context: Context) :
    ListAdapter<AssetItem, RecyclerView.ViewHolder>(AssetListDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> AssetVH.create(parent)
            0 -> EmptyVH.create(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is AssetVH -> {
                getItem(position)?.let {
                    holder.bind(context, it as AssetUI, position, this)
                }
            }
        }

//        val isExpanded = position == mExpandedPosition
//        holder.binding.layoutAsset2.visibility = if (isExpanded) View.VISIBLE else View.GONE
//        holder.binding.root.isActivated = isExpanded
//        holder.binding.root.setOnClickListener {
//            mExpandedPosition = if (isExpanded) -1 else position
//            notifyItemChanged(position)
//
//        }
//        if (getItem(position).coinName == "vis")
//            holder.binding.root.visibility = View.INVISIBLE

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AssetUI -> 1
            is EmptyUI -> 0
        }
    }
}

//var mExpandedPosition = -1
class AssetVH private constructor(val binding: AssetListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var asset: AssetUI
    fun bind(
        context: Context,
        asset: AssetUI,
        position: Int,
        adapter: ListAdapter<AssetItem, RecyclerView.ViewHolder>
    ) {
        //animated expansion
        binding.layoutAsset2.visibility =
            if (asset.visibility) View.VISIBLE else View.GONE

        binding.root.setOnClickListener {
            asset.visibility = asset.visibility == false
            adapter.notifyItemChanged(position)
        }
        //animated expansion
        this.asset = asset
        binding.holder = this
        binding.context = context
    }

    companion object {
        fun create(parent: ViewGroup): AssetVH {
            val binding =
                AssetListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return AssetVH(binding)
        }
    }
}

class EmptyVH private constructor(val binding: AssetListItemEmptyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): EmptyVH {
            val binding =
                AssetListItemEmptyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return EmptyVH(binding)
        }
    }
}


class AssetListDiffUtil : DiffUtil.ItemCallback<AssetItem>() {
    override fun areItemsTheSame(oldItem: AssetItem, newItem: AssetItem): Boolean {
        return if (oldItem is AssetUI && newItem is AssetUI)
            oldItem.coinName == newItem.coinName
        else
            true
    }

    override fun areContentsTheSame(oldItem: AssetItem, newItem: AssetItem): Boolean {
        return oldItem == newItem
    }

}
