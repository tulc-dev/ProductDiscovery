package com.tulc.product.discovery.paging


enum class PagingState {
    NO_DATA,
    LOADING_INITIAL,
    FAILED_INITIAL,
    LOADING,
    FAILED,
    LOADED,
}
