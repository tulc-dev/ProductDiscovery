package com.tulc.product.discovery.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController


class CartViewModel(val quantityCart: MutableLiveData<Int>) : ViewModel() {

    fun onBackPress(v: View) {
        v.findNavController().popBackStack()
    }

    fun onClearCart() {
        quantityCart.value = 0
    }
}
