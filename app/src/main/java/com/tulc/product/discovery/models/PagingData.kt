package com.tulc.product.discovery.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
class PagingData(
    @Json(name = "totalItems") val totalItems: Int,
    @Json(name = "page") val page: Int,
    @Json(name = "pageSize") val pageSize: Int
)
