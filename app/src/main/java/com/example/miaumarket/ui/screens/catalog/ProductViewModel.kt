package com.example.miaumarket.ui.screens.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miaumarket.data.remote.dto.ProductResponse
import com.example.miaumarket.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductResponse>>(emptyList())
    val products: StateFlow<List<ProductResponse>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var currentPage = 1
    private var isLastPage = false

    init {
        loadProducts()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        resetPagination()
        loadProducts()
    }

    private fun resetPagination() {
        currentPage = 1
        isLastPage = false
        _products.value = emptyList()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = productRepository.getProducts(currentPage, 20, _searchQuery.value.takeIf { it.isNotBlank() })
            result.onSuccess { response ->
                _products.value += response.items
                isLastPage = response.items.size < 20
            }.onFailure { e ->
                _error.value = e.message ?: "No se han podido cargar los productos"
            }
            _isLoading.value = false
        }
    }

    private val _selectedProduct = MutableStateFlow<ProductResponse?>(null)
    val selectedProduct: StateFlow<ProductResponse?> = _selectedProduct.asStateFlow()

    fun getProductById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = productRepository.getProductById(id)
            result.onSuccess { product ->
                _selectedProduct.value = product
            }.onFailure { e ->
                _error.value = e.message ?: "No se han podido cargar los detalles del producto"
            }
            _isLoading.value = false
        }
    }
}
