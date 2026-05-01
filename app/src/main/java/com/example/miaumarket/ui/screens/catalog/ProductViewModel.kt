package com.example.miaumarket.ui.screens.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miaumarket.core.data.remote.dto.ProductResponse
import com.example.miaumarket.core.data.remote.dto.ProductRequest
import com.example.miaumarket.core.data.domain.model.CartItem
import com.example.miaumarket.core.data.domain.repository.AuthRepository
import com.example.miaumarket.core.data.domain.repository.CartRepository
import com.example.miaumarket.core.data.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private companion object {
        const val PAGE_SIZE = 20
    }

    val isLoggedIn: StateFlow<Boolean> = authRepository.getAuthToken()
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _userRole = MutableStateFlow<String?>(null)
    val isAdmin: StateFlow<Boolean> = _userRole
        .map { it == "admin" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _products = MutableStateFlow<List<ProductResponse>>(emptyList())
    val products: StateFlow<List<ProductResponse>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var currentPage = 0
    private var isLastPage = false

    init {
        loadNextPage()
        checkUserRole()
    }

    private fun checkUserRole() {
        viewModelScope.launch {
            authRepository.getCurrentUser().onSuccess { user ->
                _userRole.value = user.role
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _userRole.value = null
        }
    }

    fun onSearchQueryChange(query: String) {
        if (query == _searchQuery.value) return
        _searchQuery.value = query
        resetPagination()
        loadNextPage()
    }

    private fun resetPagination() {
        currentPage = 0
        isLastPage = false
        _products.value = emptyList()
    }

    fun loadNextPage() {
        if (_isLoading.value || isLastPage) return

        val nextPage = currentPage + 1
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _error.value = null
                val result = productRepository.getProducts(
                    page = nextPage,
                    size = PAGE_SIZE,
                    search = _searchQuery.value.takeIf { it.isNotBlank() }
                )
                result.onSuccess { response ->
                    _products.value += response.items
                    currentPage = nextPage
                    isLastPage = response.items.size < PAGE_SIZE || _products.value.size >= response.total
                }.onFailure { e ->
                    _error.value = e.message ?: "No se han podido cargar los productos"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _selectedProduct = MutableStateFlow<ProductResponse?>(null)
    val selectedProduct: StateFlow<ProductResponse?> = _selectedProduct.asStateFlow()

    fun getProductById(id: Long) {
        _selectedProduct.value = null
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _error.value = null
                val result = productRepository.getProductById(id)
                result.onSuccess { product ->
                    _selectedProduct.value = product
                }.onFailure { e ->
                    _error.value = e.message ?: "No se han podido cargar los detalles del producto"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToCart(product: ProductResponse) {
        cartRepository.addToCart(
            CartItem(
                productId = product.id,
                name = product.name,
                price = product.price,
                currency = product.currency,
                imageUrl = product.imageUrl
            )
        )
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            productRepository.deleteProduct(id).onSuccess {
                _products.value = _products.value.filter { it.id != id }
                if (_selectedProduct.value?.id == id) {
                    _selectedProduct.value = null
                }
            }.onFailure { e ->
                _error.value = e.message
            }
        }
    }

    fun createProduct(product: ProductRequest) {
        viewModelScope.launch {
            productRepository.createProduct(product).onSuccess {
                resetPagination()
                loadNextPage()
            }.onFailure { e ->
                _error.value = e.message
            }
        }
    }

    fun updateProduct(id: Long, product: ProductRequest) {
        viewModelScope.launch {
            productRepository.updateProduct(id, product).onSuccess { updated ->
                _products.value = _products.value.map { if (it.id == id) updated else it }
                if (_selectedProduct.value?.id == id) {
                    _selectedProduct.value = updated
                }
            }.onFailure { e ->
                _error.value = e.message
            }
        }
    }
}
