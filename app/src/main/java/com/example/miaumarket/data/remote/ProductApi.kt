package com.example.miaumarket.data.remote

import com.example.miaumarket.data.remote.dto.ProductListResponse
import com.example.miaumarket.data.remote.dto.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("api/products")
    suspend fun getProducts(
        @Query("skip") skip: Int = 0,
        @Query("take") take: Int = 20,
        @Query("search") search: String? = null
    ): ProductListResponse

    @GET("api/products/{id}")
    suspend fun getProductById(@Path("id") id: Long): ProductResponse
}
