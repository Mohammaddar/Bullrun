package com.example.bullrun.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bullrun.data.database.model.CoinList

@Dao
interface CoinListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coinLists: List<CoinList>)

    @Query("SELECT * FROM coins_list_table WHERE name LIKE :query OR symbol LIKE :query")
    suspend fun getAll(query: String): List<CoinList>

    @Query("DELETE FROM coins_list_table")
    suspend fun clearAll()

    @Query("SELECT CASE WHEN EXISTS(SELECT 1 FROM coins_list_table) THEN 0 ELSE 1 END AS IsEmpty;")
    suspend fun isTableEmpty():Int
}
