package com.example.bullrun.ui.fragments.wallet

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bullrun.databinding.AssetListItemBinding
import com.example.bullrun.databinding.AssetListItemEmptyBinding
import com.example.bullrun.setupLineChart2
import com.example.bullrun.ui.model.HoldingItem
import com.example.bullrun.ui.model.HoldingUI
import com.example.bullrun.ui.model.EmptyUI

class AssetListAdapter(val context: Context) :
    ListAdapter<HoldingItem, RecyclerView.ViewHolder>(AssetListDiffUtil()) {
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
                    holder.bind(context, it as HoldingUI)
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
            is HoldingUI -> 1
            is EmptyUI -> 0
        }
    }
}

class AssetVH private constructor(val binding: AssetListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var asset: HoldingUI
    fun bind(
        context: Context,
        asset: HoldingUI,
    ) {
        //CoroutineScope(Dispatchers.Default).launch {
        this@AssetVH.asset = asset
        binding.holder = this@AssetVH
        binding.context = context
        val rgb=asset.theme.split(',')
        val theme=Color.argb(50, rgb[0].toInt(), rgb[1].toInt(), rgb[2].toInt())
        val themeDark= Color.argb(70, rgb[0].toInt(), rgb[1].toInt(), rgb[2].toInt())
        binding.card.setCardBackgroundColor(theme)
        binding.bottomLayout.setBackgroundColor(themeDark)

//        var theme: Int = 0
//        var themeDark: Int = 0
//        when (asset.theme) {
//            0 -> {
//                theme = ContextCompat.getColor(context, R.color.bg_gray)
//                themeDark = ContextCompat.getColor(context, R.color.bg_gray_dark)
//            }
//            1 -> {
//                theme = ContextCompat.getColor(context, R.color.bg_green)
//                themeDark = ContextCompat.getColor(context, R.color.bg_green_dark)
//            }
//            2 -> {
//                theme = ContextCompat.getColor(context, R.color.bg_orange)
//                themeDark = ContextCompat.getColor(context, R.color.bg_orange_dark)
//            }
//            3 -> {
//                theme = ContextCompat.getColor(context, R.color.bg_yellow)
//                themeDark = ContextCompat.getColor(context, R.color.bg_yellow_dark)
//            }
//            4 -> {
//                theme = ContextCompat.getColor(context, R.color.bg_blue)
//                themeDark = ContextCompat.getColor(context, R.color.bg_blue_dark)
//            }
//        }
        //binding.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.gray_700))
        setupLineChart2(context, binding.chart, asset.tickers, rgb)
        Log.d("TAGH", "${Thread.currentThread().name}   0")
        //val rgb = imageUrl2(binding.imageView3, asset.imageURL, context,asset.coinName,binding.card)
        Log.d("TAGH", "${Thread.currentThread().name}   00")


//        val dp = 48f
//        val r: Resources = context.resources
//        val px = TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP,
//            dp,
//            r.displayMetrics
//        )
//        binding.progressBarVolume.layoutParams.width= (binding.card.width-px).toInt()
        binding.guideline41.setGuidelinePercent(0.4f)
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


class AssetListDiffUtil : DiffUtil.ItemCallback<HoldingItem>() {
    override fun areItemsTheSame(oldItem: HoldingItem, newItem: HoldingItem): Boolean {
        return if (oldItem is HoldingUI && newItem is HoldingUI)
            oldItem.coinName == newItem.coinName
        else
            true
    }

    override fun areContentsTheSame(oldItem: HoldingItem, newItem: HoldingItem): Boolean {
        return oldItem == newItem
    }

}
