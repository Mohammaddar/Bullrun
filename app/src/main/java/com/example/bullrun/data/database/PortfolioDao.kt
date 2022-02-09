package com.example.bullrun.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bullrun.data.database.model.Asset
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {

    @Query("SELECT COUNT(1) FROM portfolio_table WHERE coinId = :coinId")
    suspend fun isAssetExisted(coinId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAsset(asset: Asset)

    @Query("SELECT * FROM portfolio_table WHERE coinId =:coinId")
    suspend fun getAssetById(coinId: String): Asset?

    @Query("SELECT * FROM portfolio_table")
    fun getAll(): Flow<List<Asset>>

    @Query("UPDATE portfolio_table SET current_price=:currentPrice WHERE coinId=:coinId;")
    suspend fun updateCurrentPrice(coinId: String, currentPrice: Double)

    @Query(
        "UPDATE portfolio_table SET total_buying_volume = total_buying_volume+:volume," +
                " total_buying_cost = total_buying_cost+:cost" +
                " WHERE coinId=:coinId;"
    )
    suspend fun buyAsset(coinId: String, volume: Double, cost: Double)

    @Query(
        "UPDATE portfolio_table SET total_selling_volume = total_selling_volume+:volume," +
                " total_selling_income = total_selling_income+:income" +
                " WHERE coinId=:coinId;"
    )
    suspend fun sellAsset(coinId: String, volume: Double, income: Double)

}
