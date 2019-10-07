package com.tulc.product.discovery.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ProductListing(
    @Json(name = "products") val products: List<Product>
)
