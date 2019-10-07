package com.tulc.product.discovery.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable


@JsonClass(generateAdapter = true)
data class Price(
    @Json(name = "supplierSalePrice") val supplierSalePrice: Long?,
    @Json(name = "sellPrice") val sellPrice: Long?
) : Serializable
