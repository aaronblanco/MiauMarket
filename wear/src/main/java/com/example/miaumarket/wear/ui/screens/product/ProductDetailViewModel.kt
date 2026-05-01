package com.example.miaumarket.wear.ui.screens.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miaumarket.core.data.domain.repository.ProductRepository
import com.example.miaumarket.core.data.remote.dto.ProductResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductDetailUiState(
    val product: ProductResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAddedToCart: Boolean = false
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: Long = savedStateHandle["productId"] ?: 0L

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState

    init {
        loadProductDetail()
    }

    private fun loadProductDetail() {
        if (productId == 0L) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = productRepository.getProductById(productId)

            result.onSuccess { product ->
                _uiState.value = _uiState.value.copy(
                    product = product,
                    isLoading = false
                )
            }

            result.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Error desconocido"
                )
            }
        }
    }

    fun addToCart() {
        _uiState.value = _uiState.value.copy(isAddedToCart = true)
        // Simulación de añadido al carrito
        // En una implementación real, esto interactuaría con CartRepository
    }

    fun retry() {
        loadProductDetail()
    }
}

