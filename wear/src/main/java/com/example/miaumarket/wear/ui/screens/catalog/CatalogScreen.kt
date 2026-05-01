package com.example.miaumarket.wear.ui.screens.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import coil.compose.AsyncImage

@Composable
fun CatalogScreen(
    onProductClick: (Long) -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        when {
            uiState.isLoading && uiState.products.isEmpty() -> {
                LoadingScreen()
            }
            uiState.error != null && uiState.products.isEmpty() -> {
                ErrorScreen(
                    error = uiState.error,
                    onRetry = { viewModel.retry() }
                )
            }
            uiState.products.isEmpty() -> {
                EmptyScreen()
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(uiState.products) { product ->
                        ProductCard(
                            product = product,
                            onClick = { onProductClick(product.id) }
                        )
                    }

                    if (uiState.canLoadMore && !uiState.isLoading) {
                        item {
                            Button(
                                onClick = { viewModel.loadNextPage() },
                                modifier = Modifier.fillMaxWidth(0.9f)
                            ) {
                                Text("Cargar más")
                            }
                        }
                    }

                    if (uiState.isLoading) {
                        item {
                            LoadingIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: com.example.miaumarket.core.data.remote.dto.ProductResponse,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(80.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = product.name,
                    fontSize = 12.sp,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (product.price != null) {
                    Text(
                        text = "€${String.format("%.2f", product.price)}",
                        fontSize = 10.sp,
                        color = Color(0xFFFF6B6B)
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator()
    }
}

@Composable
fun LoadingIndicator() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Cargando...", fontSize = 14.sp)
    }
}

@Composable
fun ErrorScreen(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Error,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                tint = Color(0xFFFF4444)
            )
            Text(
                text = error,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
fun EmptyScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No hay productos disponibles",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

