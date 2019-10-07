package com.tulc.product.discovery.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable


@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "url") val url: String,
    @Json(name = "priority") val priority: Int,
    @Json(name = "path") val path: String?
) : Serializable
