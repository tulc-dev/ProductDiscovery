package com.tulc.product.discovery.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.tulc.product.discovery.api.ApiClient
import com.tulc.product.discovery.extensions.subscribeMainThread
import com.tulc.product.discovery.models.Product
import com.tulc.product.discovery.utils.*
import io.reactivex.functions.Consumer
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.Executors


class ProductDataSource(private val query: MutableLiveData<String>) :
    PageKeyedDataSource<Int, Product>() {

    // paging state, listen at View
    val pagingState = MutableLiveData<PagingState>()
    // error code when call api, listen at View
    val errorCode = MutableLiveData<Int>()

    private val retryExecutor = Executors.newSingleThreadExecutor()
    private var retry: (() -> Any)? = null

    // retry when call api error
    fun invokeRetry() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            pagingState.postValue(PagingState.LOADING)
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Product>
    ) {
        Log.d(TAG, "ProductDataSource search page 1: ${query.value}")
        pagingState.postValue(PagingState.LOADING_INITIAL)

        ApiClient.getInstance().getProductList(query.value ?: "", 1, params.requestedLoadSize)
            .subscribeMainThread(
                Consumer {
                    if (it.code != API_SUCCESS) {
                        // CALL API ERROR
                        retry = {
                            loadInitial(params, callback)
                        }
                        errorCode.value = ERROR_CODE_UNKNOWN
                        pagingState.postValue(PagingState.FAILED_INITIAL)
                        return@Consumer
                    }

                    // CALL API SUCCESS
                    if (it.result.products.size < params.requestedLoadSize) {
                        callback.onResult(it.result.products, null, null)
                        pagingState.postValue(
                            if (it.result.products.isEmpty()) PagingState.NO_DATA
                            else PagingState.LOADED
                        )
                    } else {
                        callback.onResult(it.result.products, null, 2)
                        pagingState.postValue(PagingState.LOADING)
                    }
                },
                Consumer {
                    Log.d(TAG, "ProductDataSource has error ${it.message}", it)
                    when (it) {
                        is SocketTimeoutException -> errorCode.postValue(ERROR_CODE_TIME_OUT)
                        is UnknownHostException -> errorCode.postValue(
                            ERROR_CODE_NO_INTERNET_CONNECTION
                        )
                        else -> errorCode.value = ERROR_CODE_UNKNOWN
                    }
                    retry = {
                        loadInitial(params, callback)
                    }
                    pagingState.postValue(PagingState.FAILED_INITIAL)
                }
            )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
        Log.d(TAG, "ProductDataSource search page ${params.key}: ${query.value}")
        ApiClient.getInstance()
            .getProductList(query.value ?: "", params.key, params.requestedLoadSize)
            .subscribeMainThread(
                Consumer {
                    if (it.code != API_SUCCESS) {
                        // CALL API ERROR
                        retry = {
                            loadAfter(params, callback)
                        }
                        errorCode.value = ERROR_CODE_UNKNOWN
                        pagingState.postValue(PagingState.FAILED)
                    }

                    // CALL API SUCCESS
                    if (it.result.products.size < params.requestedLoadSize) {
                        callback.onResult(it.result.products, null)
                        pagingState.postValue(PagingState.LOADED)
                    } else {
                        callback.onResult(it.result.products, params.key + 1)
                    }
                },
                Consumer {
                    Log.d(TAG, "ProductDataSource has error ${it.message}", it)
                    when (it) {
                        is SocketTimeoutException -> errorCode.postValue(ERROR_CODE_TIME_OUT)
                        is UnknownHostException -> errorCode.postValue(
                            ERROR_CODE_NO_INTERNET_CONNECTION
                        )
                        else -> errorCode.value = ERROR_CODE_UNKNOWN
                    }
                    retry = {
                        loadAfter(params, callback)
                    }
                    pagingState.postValue(PagingState.FAILED)
                }
            )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
        // don't use 
    }
}
