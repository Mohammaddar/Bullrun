package com.example.bullrun.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bullrun.data.database.model.Transaction

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(transaction: Transaction)

    @Query("SELECT * FROM transaction_table WHERE walletName=:walletName AND coinId=:assetName")
    suspend fun getTransactionByAssetAndWallet(assetName:String,walletName:String):List<Transaction>

}
