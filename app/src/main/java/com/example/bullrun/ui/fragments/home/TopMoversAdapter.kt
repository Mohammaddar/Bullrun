package com.example.bullrun.ui.fragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bullrun.databinding.TopMoverItemBinding
import com.example.bullrun.ui.model.TopMoverUi

class TopMoversAdapter(val context: Context,val itemWidth:Int,private val onClick: (topMoverUi: TopMoverUi) -> Unit) :
    ListAdapter<TopMoverUi, TopMoverVH>(TopMoverDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopMoverVH {
        return TopMoverVH.create(parent)
    }

    override fun onBindViewHolder(holder: TopMoverVH, position: Int) {
        getItem(position)?.let { holder.bind(it,context,itemWidth,position,onClick) }
    }
}

class TopMoverVH private constructor(private val binding: TopMoverItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var onClick: (topMoverUi: TopMoverUi) -> Unit
    fun bind(topMoverUi: TopMoverUi, context: Context, itemWidth: Int, position: Int, onClick: (topMoverUi: TopMoverUi) -> Unit) {
        if (topMoverUi.coinId=="PlaceHolder")
            return
        binding.topMover=topMoverUi
        binding.txtRank.text="#${position+1}"
        binding.cardTopMover.layoutParams.width=itemWidth
    }

    fun onClickListener(topMoverUi: TopMoverUi) = onClick(topMoverUi)

    companion object {
        fun create(parent: ViewGroup): TopMoverVH {
            val binding =
                TopMoverItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TopMoverVH(binding)
        }
    }
}

class TopMoverDiffUtil : DiffUtil.ItemCallback<TopMoverUi>() {
    override fun areItemsTheSame(oldItem: TopMoverUi, newItem: TopMoverUi): Boolean {
        return oldItem.coinName.equals(newItem.coinName)
    }

    override fun areContentsTheSame(oldItem: TopMoverUi, newItem: TopMoverUi): Boolean {
        return oldItem == newItem
    }

}
