package com.example.miaumarket.domain.model

data class CartItem(
    val productId: Long,
    val name: String,
    val price: Double?,
    val currency: String,
    val imageUrl: String?,
    val quantity: Int = 1
)
