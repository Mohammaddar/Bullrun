package com.example.bullrun.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bullrun.data.database.model.Asset
import com.example.bullrun.data.database.model.Wallet
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {

    @Query("SELECT COUNT(1) FROM portfolio_table WHERE coinId = :coinId AND walletName= :walletName")
    suspend fun isAssetExisted(coinId: String,walletName:String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAsset(asset: Asset)

    @Query("SELECT * FROM portfolio_table WHERE walletName=:walletName")
    fun getAllAssetsInWalletFlow(walletName: String): Flow<List<Asset>>

    @Query("SELECT coinId FROM portfolio_table WHERE walletName=:walletName")
    suspend fun getAllAssetsIDsInWallet(walletName: String): List<String>

    @Query("UPDATE portfolio_table SET current_price=:currentPrice, price_change_percentage_24h=:priceChangePercentage24H WHERE coinId=:coinId;")
    suspend fun updatePriceInfo(coinId: String, currentPrice: Double,priceChangePercentage24H:Double)

    @Query(
        "UPDATE portfolio_table SET total_buying_volume = total_buying_volume+:volume," +
                " total_buying_cost = total_buying_cost+:cost" +
                " WHERE coinId=:coinId AND walletName= :walletName;"
    )
    suspend fun buyAsset(coinId: String, volume: Double, cost: Double,walletName: String)

    @Query(
        "UPDATE portfolio_table SET total_selling_volume = total_selling_volume+:volume," +
                " total_selling_income = total_selling_income+:income" +
                " WHERE coinId=:coinId AND walletName= :walletName;"
    )
    suspend fun sellAsset(coinId: String, volume: Double, income: Double,walletName: String)

    @Query("SELECT * FROM portfolio_table")
    fun getAllAssetsFlow():Flow<List<Asset>>

    @Query("SELECT coinId FROM portfolio_table")
    suspend fun getAllAssetsIds():List<String>
}
