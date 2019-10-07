package com.tulc.product.discovery.api

import com.tulc.product.discovery.models.ProductDetail
import com.tulc.product.discovery.models.ProductListing
import com.tulc.product.discovery.models.Response
import com.tulc.product.discovery.utils.PAGE_SIZE
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {

    @GET("/api/search")
    fun getProductList(
        @Query("q") query: String = "",
        @Query("_page") page: Int = 1,
        @Query("_limit") limit: Int = PAGE_SIZE,
        @Query("visitorId") visitorId: String = "",
        @Query("channel") channel: String = "pv_online",
        @Query("terminal") terminal: String = "CP01"
    ): Single<Response<ProductListing>>

    @GET("/api/products/{product_sku}")
    fun getProductDetail(
        @Path("product_sku") sku: String,
        @Query("visitorId") visitorId: String = "",
        @Query("channel") channel: String = "pv_online",
        @Query("terminal") terminal: String = "CP01"
    ): Single<Response<ProductDetail>>
}
