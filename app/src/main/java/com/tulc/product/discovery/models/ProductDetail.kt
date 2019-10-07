package com.tulc.product.discovery.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
class ProductDetail(
    @Json(name = "product") val product: Product
)
