package com.tulc.product.discovery.data

import androidx.lifecycle.LiveData
import com.tulc.product.discovery.models.RecentSearch


class RecentSearchRepository(private val recentSearchDao: RecentSearchDao) {

    fun getRecentSearches(input: String?): LiveData<List<RecentSearch>> =
        recentSearchDao.getRecentSearches(input)

    suspend fun insert(recentSearch: RecentSearch) {
        recentSearchDao.insert(recentSearch)
    }

    suspend fun delete(recentSearch: RecentSearch) {
        recentSearchDao.delete(recentSearch)
    }
}