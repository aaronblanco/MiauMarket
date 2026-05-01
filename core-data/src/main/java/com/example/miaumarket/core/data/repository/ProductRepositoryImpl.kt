package com.example.miaumarket.core.data.repository

import com.example.miaumarket.core.data.remote.ProductApi
import com.example.miaumarket.core.data.remote.dto.ProductListResponse
import com.example.miaumarket.core.data.remote.dto.ProductResponse
import com.example.miaumarket.core.data.remote.dto.ProductRequest
import com.example.miaumarket.core.data.domain.repository.ProductRepository
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e.toUserFacingException("No se han podido cargar los productos"))
        }
    }

    override suspend fun getProductById(id: Long): Result<ProductResponse> {
        return try {
            val response = productApi.getProductById(id)
            Result.success(response)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e.toUserFacingException("No se han podido cargar los detalles del producto"))
        }
    }

    override suspend fun createProduct(product: ProductRequest): Result<ProductResponse> {
        return try {
            val response = productApi.createProduct(product)
            Result.success(response)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e.toUserFacingException("No se ha podido crear el producto"))
        }
    }

    override suspend fun updateProduct(id: Long, product: ProductRequest): Result<ProductResponse> {
        return try {
            val response = productApi.updateProduct(id, product)
            Result.success(response)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e.toUserFacingException("No se ha podido actualizar el producto"))
        }
    }

    override suspend fun deleteProduct(id: Long): Result<Unit> {
        return try {
            productApi.deleteProduct(id)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e.toUserFacingException("No se ha podido eliminar el producto"))
        }
    }

    private fun Throwable.toUserFacingException(defaultMessage: String): Throwable {
        val message = when (this) {
            is HttpException -> {
                response()?.errorBody()?.string()
                    ?.extractBackendMessage()
                    ?: when (code()) {
                        401 -> "Credenciales incorrectas o sesión caducada"
                        403 -> "No tienes permisos para acceder a este recurso"
                        404 -> "No se ha encontrado el producto solicitado"
                        in 500..599 -> "El servidor no está disponible en este momento"
                        else -> defaultMessage
                    }
            }
            is IOException -> "No se ha podido conectar con el servidor"
            else -> message?.takeIf { it.isNotBlank() } ?: defaultMessage
        }

        return IllegalStateException(message, this)
    }

    private fun String.extractBackendMessage(): String? {
        val trimmed = trim()
        if (trimmed.isEmpty()) return null

        Regex("\"message\"\\s*:\\s*\"([^\"]+)\"").find(trimmed)?.groupValues?.getOrNull(1)?.let { return it }
        Regex("\"error\"\\s*:\\s*\"([^\"]+)\"").find(trimmed)?.groupValues?.getOrNull(1)?.let { return it }

        return trimmed.removePrefix("{").removeSuffix("}").takeIf { it.isNotBlank() }
    }
}

