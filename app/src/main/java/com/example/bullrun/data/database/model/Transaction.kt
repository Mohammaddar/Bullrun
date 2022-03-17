package com.example.bullrun.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,

    var type:String,//Buy or Sell

    var coinId:String,

    var volume:Double,

    var price:Double,

    var walletName: String,

    var dateMillis:Long
)
