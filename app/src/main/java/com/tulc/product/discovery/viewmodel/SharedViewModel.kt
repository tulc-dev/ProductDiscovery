package com.tulc.product.discovery.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SharedViewModel : ViewModel() {
    
    val quantityCart = MutableLiveData(0)
}
