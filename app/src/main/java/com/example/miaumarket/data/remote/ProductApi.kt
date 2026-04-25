package com.example.miaumarket.data.remote

import com.example.miaumarket.data.remote.dto.ProductListResponse
import com.example.miaumarket.data.remote.dto.ProductResponse
import com.example.miaumarket.data.remote.dto.ProductRequest
import retrofit2.http.*

interface ProductApi {
    @GET("api/products")
    suspend fun getProducts(
        @Query("skip") skip: Int = 0,
        @Query("take") take: Int = 20,
        @Query("search") search: String? = null
    ): ProductListResponse

    @GET("api/products/{id}")
    suspend fun getProductById(@Path("id") id: Long): ProductResponse

    @POST("api/products")
    suspend fun createProduct(@Body product: ProductRequest): ProductResponse

    @PUT("api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Long, @Body product: ProductRequest): ProductResponse

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long)
}
