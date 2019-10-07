package com.tulc.product.discovery.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Response<T>(
    @Json(name = "code") val code: String,
    @Json(name = "result") val result: T,
    @Json(name = "extra") val extra: PagingData?
)
