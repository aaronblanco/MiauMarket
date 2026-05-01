package com.example.miaumarket.core.data.domain.repository

import com.example.miaumarket.core.data.remote.dto.ProductListResponse
import com.example.miaumarket.core.data.remote.dto.ProductResponse
import com.example.miaumarket.core.data.remote.dto.ProductRequest

interface ProductRepository {
    suspend fun getProducts(page: Int, size: Int, search: String?): Result<ProductListResponse>
    suspend fun getProductById(id: Long): Result<ProductResponse>
    suspend fun createProduct(product: ProductRequest): Result<ProductResponse>
    suspend fun updateProduct(id: Long, product: ProductRequest): Result<ProductResponse>
    suspend fun deleteProduct(id: Long): Result<Unit>
}

