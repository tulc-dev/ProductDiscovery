package com.tulc.product.discovery.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable


@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = "displayName") val displayName: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "sku") val sku: String,
    @Json(name = "totalAvailable") val totalAvailable: Int?,
    @Json(name = "price") val price: Price,
    @Json(name = "images") val images: List<Image>?,
    @Json(name = "attributeGroups") val attributeGroups: List<Attribute>?
) : Serializable {

    fun getDisplay(): String {
        return if (name.isNullOrEmpty()) {
            displayName ?: ""
        } else {
            name
        }
    }

    fun getPercentageDiscount(): Int {
        if (price.sellPrice == null || price.supplierSalePrice == null) return 0
        return 100 - (price.sellPrice * 100 / price.supplierSalePrice).toInt()
    }

    fun getSellPrice(): String {
        price.sellPrice?.let {
            return "%,d".format(it).replace(',', '.')
        }
        return ""
    }

    fun getListedPrice(): String {
        price.supplierSalePrice?.let {
            return "%,d".format(it).replace(',', '.')
        }
        return ""
    }

    fun getImageUrl(): String? {
        images?.let {
            return if (it.isEmpty()) null
            else it[0].url
        }
        return null
    }

    fun isAvailable() = totalAvailable ?: 0 > 0 && (price.sellPrice ?: 0) > 0
}
