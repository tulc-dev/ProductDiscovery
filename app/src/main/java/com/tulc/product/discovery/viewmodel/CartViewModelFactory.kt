package com.tulc.product.discovery.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class CartViewModelFactory(
    private val quantityCart: MutableLiveData<Int>
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CartViewModel(quantityCart) as T
    }
}
