package com.example.miaumarket.domain.repository

import com.example.miaumarket.data.remote.dto.ProductListResponse
import com.example.miaumarket.data.remote.dto.ProductResponse

interface ProductRepository {
    suspend fun getProducts(page: Int, size: Int, search: String?): Result<ProductListResponse>
    suspend fun getProductById(id: String): Result<ProductResponse>
}
