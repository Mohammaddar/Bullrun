package com.example.bullrun.data.database

import androidx.paging.PagingSource
import androidx.room.*
import com.example.bullrun.data.database.model.Coin

@Dao
interface CoinDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coins: List<Coin>)

    @Query("SELECT * FROM coins_table WHERE name LIKE :query ORDER BY market_cap_rank")
    fun getAll(query:String): PagingSource<Int, Coin>

    @Query("DELETE FROM coins_table")
    suspend fun clearAll():Int


}

