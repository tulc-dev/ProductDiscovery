package com.tulc.product.discovery.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.tulc.product.discovery.R
import com.tulc.product.discovery.api.ApiClient
import com.tulc.product.discovery.extensions.showMessage
import com.tulc.product.discovery.extensions.subscribeMainThread
import com.tulc.product.discovery.models.Product
import com.tulc.product.discovery.ui.ProductDetailFragment
import com.tulc.product.discovery.utils.*
import io.reactivex.functions.Consumer
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.collections.ArrayList


class ProductDetailViewModel(val product: Product, val quantityCart: MutableLiveData<Int>) :
    ViewModel() {

    private val productLiveData = MutableLiveData<Product>(product)

    val title = Transformations.map(productLiveData) {
        it.getDisplay()
    }

    val price = Transformations.map(productLiveData) {
        it.getSellPrice()
    }

    val listedPrice = Transformations.map(productLiveData) {
        it.getListedPrice()
    }

    val percentageDiscount = Transformations.map(productLiveData) {
        it.getPercentageDiscount()
    }

    val sku = Transformations.map(productLiveData) {
        it.sku
    }

    val images = Transformations.map(productLiveData) {
        it.images ?: ArrayList()
    }

    val technicals = Transformations.map(productLiveData) {
        it.attributeGroups ?: ArrayList()
    }

    val goneImageIndicator = Transformations.map(productLiveData) {
        (it.images ?: ArrayList()).size <= 1
    }

    val available = Transformations.map(productLiveData) {
        it.isAvailable()
    }

    val tabSelection = MutableLiveData(ProductDetailFragment.PRODUCT_DESCRIPTION_TAB_INDEX)

    val showMore = MutableLiveData<Boolean?>(false)

    var quantityCartDisplay = Transformations.map(quantityCart) {
        when {
            it < 1 -> ""
            it > 99 -> "99+"
            else -> it.toString()
        }
    }

    var quantity = MutableLiveData(1)

    var total = MutableLiveData(product.getSellPrice())

    val loading = MutableLiveData(false)

    val errorCode = MutableLiveData<Int?>()

    fun onBackPress(v: View) {
        v.findNavController().popBackStack()
    }

    fun onNavigateCart(v: View) {
        v.findNavController().navigate(R.id.action_productDetailFragment_to_cartFragment)
    }

    fun setTabSelection(index: Int) {
        tabSelection.value = index
        when (index) {
            ProductDetailFragment.TECHNICAL_DATA_TAB_INDEX -> {
                if ((technicals.value?.size ?: 0) > 4) {
                    showMore.value = true
                } else {
                    showMore.value = null
                }
            }
            else -> {
                showMore.value = null
            }
        }
    }

    fun onShowMore() {
        val isShowLess = showMore.value
        isShowLess?.let {
            showMore.value = !isShowLess
        }
    }

    fun loadDetail() {
        loading.value = true
        errorCode.value = null
        ApiClient.getInstance().getProductDetail(product.sku).subscribeMainThread(
            Consumer {
                if (it.code != API_SUCCESS) {
                    // CALL API ERROR
                    errorCode.value = ERROR_CODE_UNKNOWN
                    return@Consumer
                }

                // CALL API SUCCESS
                productLiveData.value = it.result.product

                if ((it.result.product.attributeGroups?.size ?: 0) > 4) {
                    showMore.value = true
                } else {
                    showMore.value = null
                }

                loading.value = false
            },
            Consumer {
                Log.d(TAG, "ProductDetail has error ${it.message}", it)
                when (it) {
                    is SocketTimeoutException -> errorCode.value = ERROR_CODE_TIME_OUT
                    is UnknownHostException -> errorCode.value = ERROR_CODE_NO_INTERNET_CONNECTION
                    else -> errorCode.value = ERROR_CODE_UNKNOWN
                }
                loading.value = false
            }
        )
    }

    fun onDecrease(v: View) {
        val quantity = this.quantity.value!! - 1
        if (quantity == 0) return
        this.quantity.value = quantity
        productLiveData.value?.price?.sellPrice?.let {
            total.value = "%,d".format(it * quantity).replace(',', '.')
        }

    }

    fun onIncrease(v: View) {
        val quantity = this.quantity.value!! + 1
        if (quantity > productLiveData.value?.totalAvailable ?: 0) {
            v.context.showMessage(
                R.string.total_available,
                productLiveData.value?.totalAvailable ?: 0
            )
            return
        }
        this.quantity.value = quantity
        productLiveData.value?.price?.sellPrice?.let {
            total.value = "%,d".format(it * quantity).replace(',', '.')
        }
    }

    fun onAddToCart() {
        quantityCart.value = (quantityCart.value ?: 0) + (quantity.value ?: 0)
    }
}
