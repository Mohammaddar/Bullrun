package com.example.bullrun.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bullrun.data.database.model.Asset
import com.example.bullrun.data.database.model.Wallet
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWallet(wallet: Wallet)

    //while using flow as output,there is no need to declare fun as suspend
    @Query("SELECT walletName FROM wallet_table")
    fun getAllNamesFlow(): Flow<List<String>>


}
