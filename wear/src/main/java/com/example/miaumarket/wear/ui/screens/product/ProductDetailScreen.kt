package com.example.miaumarket.wear.ui.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText

@Composable
fun ProductDetailScreen(
    onBackClick: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = { TimeText() },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            when {
                uiState.value.isLoading -> {
                    LoadingState()
                }
                uiState.value.error != null -> {
                    val errorMsg = uiState.value.error
                    ErrorState(
                        error = errorMsg,
                        onRetry = { viewModel.retry() },
                        onBack = onBackClick
                    )
                }
                uiState.value.product != null -> {
                    ProductContent(
                        product = uiState.value.product!!,
                        isAddedToCart = uiState.value.isAddedToCart,
                        onAddToCart = { viewModel.addToCart() },
                        onBack = onBackClick,
                        listState = listState
                    )
                }
            }
        }
    }
}

@Composable
fun ProductContent(
    product: com.example.miaumarket.core.data.remote.dto.ProductResponse,
    isAddedToCart: Boolean,
    onAddToCart: () -> Unit,
    onBack: () -> Unit,
    listState: androidx.wear.compose.foundation.lazy.ScalingLazyListState
) {
    ScalingLazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = product.name,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                maxLines = 3
            )
        }

        item {
            if (product.price != null) {
                Text(
                    text = "€${String.format("%.2f", product.price)}",
                    fontSize = 16.sp,
                    color = Color(0xFFFF6B6B),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
        }

        if (!product.source.isNullOrEmpty()) {
            item {
                Text(
                    text = "Fuente: ${product.source}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }

        item {
            Chip(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth(0.9f),
                label = { Text(if (isAddedToCart) "✓ En el carrito" else "Añadir al carrito", fontSize = 12.sp) }
            )
        }

        item {
            Chip(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(0.9f),
                label = { Text("Volver", fontSize = 12.sp) },
                colors = androidx.wear.compose.material.ChipDefaults.secondaryChipColors()
            )
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun ErrorState(
    error: String?,
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
            Text(
                text = error ?: "Error desconocido",
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
            Chip(onClick = onRetry, label = { Text("Reintentar", fontSize = 10.sp) }, modifier = Modifier.fillMaxWidth(0.9f))
            Chip(onClick = onBack, label = { Text("Atrás", fontSize = 10.sp) }, modifier = Modifier.fillMaxWidth(0.9f))
        }
    }
}






