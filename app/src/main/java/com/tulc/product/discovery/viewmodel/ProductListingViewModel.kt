package com.tulc.product.discovery.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.tulc.product.discovery.data.RecentSearchRepository
import com.tulc.product.discovery.extensions.getActivity
import com.tulc.product.discovery.paging.PagingState
import com.tulc.product.discovery.paging.ProductDataSourceFactory
import com.tulc.product.discovery.extensions.hideSoftKeyBoard
import com.tulc.product.discovery.models.Product
import com.tulc.product.discovery.models.RecentSearch
import com.tulc.product.discovery.utils.PAGE_SIZE
import kotlinx.coroutines.launch
import kotlin.system.exitProcess


class ProductListingViewModel(private val repository: RecentSearchRepository) : ViewModel() {

    val query = MutableLiveData<String>("")

    val refreshing = MutableLiveData(false)

    val recentSearches = Transformations.switchMap(query) {
        repository.getRecentSearches(query.value)
    }

    val typing = MutableLiveData(false)

    private val dataSourceFactory = ProductDataSourceFactory(query)

    private var preQuery = ""

    val pagingState = Transformations.switchMap(dataSourceFactory.sourceLiveData) {
        it.pagingState
    }

    val loading = Transformations.map(pagingState) {
        it == PagingState.LOADING_INITIAL && refreshing.value != true
    }

    val noData = Transformations.map(pagingState) {
        it == PagingState.NO_DATA
    }

    val errorCode = Transformations.switchMap(dataSourceFactory.sourceLiveData) {
        it.errorCode
    }

    val hasError = Transformations.map(pagingState) {
        it == PagingState.FAILED_INITIAL
    }


    private val config = PagedList.Config.Builder()
        .setPageSize(PAGE_SIZE)
        .setInitialLoadSizeHint(PAGE_SIZE)
        .setPrefetchDistance(1)
        .setEnablePlaceholders(false)
        .build()

    val products: LiveData<PagedList<Product>> =
        LivePagedListBuilder(dataSourceFactory, config).build()


    fun onBackPress(v: View) {
        if (typing.value == true){
            onClearQuery(v)
        } else {
            v.context.getActivity()?.onBackPressed()
        }
    }

    fun onClearQuery(v: View) {
        query.value = ""
        search(v.context)
    }

    fun onRetry(v: View) {
        search(v.context, true)
    }

    fun startTyping() {
        typing.value = true
    }

    fun deleteRecentSearch(recentSearch: RecentSearch) {
        viewModelScope.launch {
            repository.delete(recentSearch)
        }
    }

    fun search(ctx: Context?, query: String) {
        this.query.value = query
        search(ctx, true)
    }

    fun search(ctx: Context?, always: Boolean = false) {
        query.value?.let { text ->
            if (text.isNotBlank()) {
                val recentSearch = RecentSearch(text, System.currentTimeMillis())
                viewModelScope.launch {
                    repository.insert(recentSearch)
                }
            }
        }

        typing.value = false
        ctx?.hideSoftKeyBoard()
        if (preQuery != query.value || always) {
            dataSourceFactory.sourceLiveData.value?.invalidate()
            preQuery = query.value ?: ""
        }
    }

    fun invokeRetry() {
        dataSourceFactory.sourceLiveData.value?.invokeRetry()
    }
}
