package com.tulc.product.discovery.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tulc.product.discovery.models.Product


class ProductDetailViewModelFactory(
    private val product: Product,
    private val quantityCart: MutableLiveData<Int>
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductDetailViewModel(product, quantityCart) as T
    }
}
