package com.example.bullrun.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val coinId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
