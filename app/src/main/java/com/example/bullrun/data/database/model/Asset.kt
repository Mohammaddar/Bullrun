package com.example.bullrun.data.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "portfolio_table")
data class Asset(
    @PrimaryKey(autoGenerate = false)
    var coinId: String,

    @ColumnInfo(name = "name")
    val coinName: String,

    @ColumnInfo(name = "image")
    val image: String? = null,

    @ColumnInfo(name = "symbol")
    val symbol: String,

    @ColumnInfo(name = "current_price")
    val currentPrice: Double,

    @ColumnInfo(name = "total_buying_volume")
    val totalBuyingVolume: Double = 0.0,

    @ColumnInfo(name = "total_selling_volume")
    val totalSellingVolume: Double = 0.0,

    @ColumnInfo(name = "total_buying_cost")
    val totalBuyingCost: Double = 0.0,

    @ColumnInfo(name = "total_selling_income")
    val totalSellingIncome: Double = 0.0

) : Parcelable
