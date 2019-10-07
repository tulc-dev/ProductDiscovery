package com.tulc.product.discovery.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tulc.product.discovery.models.RecentSearch


@Dao
interface RecentSearchDao {

    @Query("SELECT * from recent_search_table WHERE text LIKE '%'||:input||'%' COLLATE UTF8_GENERAL_CI ORDER BY time DESC")
    fun getRecentSearches(input: String?): LiveData<List<RecentSearch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recentSearch: RecentSearch)

    @Delete
    suspend fun delete(recentSearch: RecentSearch)

    @Query("DELETE FROM recent_search_table")
    suspend fun deleteAll()
}