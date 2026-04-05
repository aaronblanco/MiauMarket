package com.example.miaumarket.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductResponse(
    @Json(name = "id") val id: String,
    @Json(name = "title") val name: String,
    @Json(name = "price") val price: Double,
    @Json(name = "image") val imageUrl: String? = null,
    @Json(name = "url") val sourceUrl: String? = null,
    @Json(name = "currency") val currency: String? = "EUR",
    @Json(name = "source") val source: String? = null
)

@JsonClass(generateAdapter = true)
data class ProductListResponse(
    val items: List<ProductResponse>
)
