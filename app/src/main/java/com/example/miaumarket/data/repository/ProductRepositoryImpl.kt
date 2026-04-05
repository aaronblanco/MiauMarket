package com.example.miaumarket.data.repository

import com.example.miaumarket.data.remote.ProductApi
import com.example.miaumarket.data.remote.dto.ProductListResponse
import com.example.miaumarket.data.remote.dto.ProductResponse
import com.example.miaumarket.domain.repository.ProductRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : ProductRepository {

    override suspend fun getProducts(page: Int, size: Int, search: String?): Result<ProductListResponse> {
        return try {
            val skip = (page - 1) * size
            val response = productApi.getProducts(skip, size, search)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductById(id: String): Result<ProductResponse> {
        return try {
            val response = productApi.getProductById(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
