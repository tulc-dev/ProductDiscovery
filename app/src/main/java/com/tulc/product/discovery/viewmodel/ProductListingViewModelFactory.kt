package com.tulc.product.discovery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tulc.product.discovery.data.RecentSearchRepository


class ProductListingViewModelFactory(
    private val repository: RecentSearchRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductListingViewModel(repository) as T
    }
}
