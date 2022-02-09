package com.example.bullrun.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins_list_table")
data class CoinList(
    @PrimaryKey(autoGenerate = false)
    var coinId: String ,

    @ColumnInfo(name = "name")
    val coinName: String? = null,

    @ColumnInfo(name = "symbol")
    val coinSymbol: String? = null,
)
