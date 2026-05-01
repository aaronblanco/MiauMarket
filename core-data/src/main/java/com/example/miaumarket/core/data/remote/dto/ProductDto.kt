package com.example.miaumarket.core.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val name: String,
    @Json(name = "price") val price: Double? = null,
    @Json(name = "rawPrice") val rawPrice: String? = null,
    @Json(name = "image") val imageUrl: String? = null,
    @Json(name = "url") val sourceUrl: String? = null,
    @Json(name = "currency") val currency: String = "EUR",
    @Json(name = "source") val source: String? = null,
    @Json(name = "scrapedAt") val scrapedAt: String? = null,
    @Json(name = "createdAt") val createdAt: String? = null,
    @Json(name = "updatedAt") val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class ProductRequest(
    @Json(name = "title") val name: String,
    @Json(name = "price") val price: Double?,
    @Json(name = "image") val imageUrl: String?,
    @Json(name = "url") val sourceUrl: String?,
    @Json(name = "currency") val currency: String = "EUR",
    @Json(name = "source") val source: String = "manual"
)

@JsonClass(generateAdapter = true)
data class ProductListResponse(
    @Json(name = "total") val total: Int = 0,
    @Json(name = "take") val take: Int = 20,
    @Json(name = "skip") val skip: Int = 0,
    @Json(name = "sortBy") val sortBy: String? = null,
    @Json(name = "sortDir") val sortDir: String? = null,
    val items: List<ProductResponse>
)

