package com.tulc.product.discovery.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.tulc.product.discovery.models.Product


class ProductDataSourceFactory(private val query: MutableLiveData<String>) :
    DataSource.Factory<Int, Product>() {

    val sourceLiveData = MutableLiveData<ProductDataSource>()

    override fun create(): DataSource<Int, Product> {
        val dataSource = ProductDataSource(query)
        sourceLiveData.postValue(dataSource)
        return dataSource
    }
}
