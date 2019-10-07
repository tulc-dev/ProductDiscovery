package com.tulc.product.discovery.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "recent_search_table")
class RecentSearch(
    @PrimaryKey @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "time") val time: Long
)
