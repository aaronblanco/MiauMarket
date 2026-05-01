package com.example.miaumarket.core.data.domain.repository

import com.example.miaumarket.core.data.domain.model.CartItem
import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    val cartItems: StateFlow<List<CartItem>>
    fun addToCart(item: CartItem)
    fun removeFromCart(productId: Long)
    fun clearCart()
}

