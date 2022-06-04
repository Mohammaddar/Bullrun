package com.example.bullrun.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    var type:TransactionType,//Buy or Sell
    var coinId:String,
    var coinName:String,
    var symbol:String,
    var volume:Double,
    var price:Double,
    var walletName: String,
    var dateMillis:Long,
    var image:String
)

enum class TransactionType(val text:String){
    Buy("BUY"),
    Sell("SELL")
}
