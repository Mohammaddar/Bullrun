package com.example.bullrun.data.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "coins_table")
data class Coin(
    @PrimaryKey(autoGenerate = false)
    var coinId: String,

    @ColumnInfo(name = "market_cap_rank")
    val marketCapRank: Long = 0,

    @ColumnInfo(name = "name")
    val coinName: String? = null,

    @ColumnInfo(name = "symbol")
    val coinSymbol: String? = null,

    @ColumnInfo(name = "price_change_percentage_24h")
    val priceChangePercentage24h: Double? = 0.0,

    @ColumnInfo(name = "current_price")
    val currentPrice: Double? = 0.0,

    @ColumnInfo(name = "image")
    val image: String? = null,
):Parcelable
