package com.example.bullrun.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "wallet_table")
data class Wallet(
    @PrimaryKey
    var walletName: String,
)
