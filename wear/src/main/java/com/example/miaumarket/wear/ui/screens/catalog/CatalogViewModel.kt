package com.example.miaumarket.wear.ui.screens.catalog

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miaumarket.core.data.domain.repository.ProductRepository
import com.example.miaumarket.core.data.remote.dto.ProductResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CatalogUiState(
    val products: List<ProductResponse> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val canLoadMore: Boolean = true
)

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(CatalogUiState())
    val uiState: State<CatalogUiState> = _uiState

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val page = _uiState.value.currentPage
            val size = 10 // Reduced for watch screen

            val result = productRepository.getProducts(page, size, null)

            result.onSuccess { response ->
                val newProducts = if (page == 1) {
                    response.items
                } else {
                    _uiState.value.products + response.items
                }

                val hasMoreItems = (page - 1) * size + response.items.size < response.total

                _uiState.value = _uiState.value.copy(
                    products = newProducts,
                    isLoading = false,
                    canLoadMore = hasMoreItems
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

    fun loadNextPage() {
        if (!_uiState.value.isLoading && _uiState.value.canLoadMore) {
            _uiState.value = _uiState.value.copy(currentPage = _uiState.value.currentPage + 1)
            loadProducts()
        }
    }

    fun retry() {
        _uiState.value = _uiState.value.copy(currentPage = 1)
        loadProducts()
    }
}

