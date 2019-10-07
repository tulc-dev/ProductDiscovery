package com.tulc.product.discovery.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable


@JsonClass(generateAdapter = true)
data class Attribute(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "value") val value: String?,
    @Json(name = "parentId") val parentId: Int?,
    @Json(name = "priority") val priority: Int?
) : Serializable
