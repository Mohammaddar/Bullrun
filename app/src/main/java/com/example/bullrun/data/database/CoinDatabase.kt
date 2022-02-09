package com.example.bullrun.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bullrun.data.database.model.Asset
import com.example.bullrun.data.database.model.Coin
import com.example.bullrun.data.database.model.CoinList
import com.example.bullrun.data.database.model.RemoteKeys

@Database(entities = [Coin::class, RemoteKeys::class, CoinList::class, Asset::class], version = 1, exportSchema = false)
abstract class CoinDatabase : RoomDatabase() {

    abstract val coinDatabaseDao: CoinDatabaseDao
    abstract val remoteKeysDao: RemoteKeysDao
    abstract val CoinListDao: CoinListDao
    abstract val portfolioDao: PortfolioDao

    companion object {

        @Volatile
        private var INSTANCE: CoinDatabase? = null

        fun getInstance(context: Context): CoinDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CoinDatabase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
