package com.example.miaumarket.wear.ui.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

@Composable
fun ProductDetailScreen(
    onBackClick: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        when {
            uiState.value.isLoading -> {
                LoadingState()
            }
            uiState.value.error != null -> {
                ErrorState(
                    error = uiState.value.error,
                    onRetry = { viewModel.retry() },
                    onBack = onBackClick
                )
            }
            uiState.value.product != null -> {
                ProductContent(
                    product = uiState.value.product!!,
                    isAddedToCart = uiState.value.isAddedToCart,
                    onAddToCart = { viewModel.addToCart() },
                    onBack = onBackClick
                )
            }
        }
    }
}

@Composable
fun ProductContent(
    product: com.example.miaumarket.core.data.remote.dto.ProductResponse,
    isAddedToCart: Boolean,
    onAddToCart: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = product.name,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            if (product.price != null) {
                Text(
                    text = "€${String.format("%.2f", product.price)}",
                    fontSize = 12.sp,
                    color = Color(0xFFFF6B6B)
                )
            }

            if (!product.sourceUrl.isNullOrEmpty()) {
                Text(
                    text = "Origen: ${product.source ?: "Desconocido"}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (product.scrapedAt != null) {
                Text(
                    text = "Actualizado: ${product.scrapedAt}",
                    fontSize = 9.sp,
                    color = Color.Gray
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth(0.9f),
                enabled = !isAddedToCart
            ) {
                Text(if (isAddedToCart) "✓ Añadido" else "Añadir al carrito")
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Text("Atrás")
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Cargando...", fontSize = 14.sp)
    }
}

@Composable
fun ErrorState(
    error: String,
    onRetry: () -> Unit,
    onBack: () -> Unit
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
            Button(onClick = onBack) {
                Text("Atrás")
            }
        }
    }
}

