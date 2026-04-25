package com.example.miaumarket.ui.screens.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.miaumarket.data.remote.dto.ProductRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    productId: Long? = null,
    viewModel: ProductViewModel,
    onBackClick: () -> Unit
) {
    val selectedProduct by viewModel.selectedProduct.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var sourceUrl by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("EUR") }
    var source by remember { mutableStateOf("manual") }

    LaunchedEffect(productId) {
        if (productId != null) {
            viewModel.getProductById(productId)
        }
    }

    LaunchedEffect(selectedProduct) {
        selectedProduct?.let {
            name = it.name
            price = it.price?.toString() ?: ""
            imageUrl = it.imageUrl ?: ""
            sourceUrl = it.sourceUrl ?: ""
            currency = it.currency
            source = it.source ?: "manual"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productId == null) "Nuevo Producto" else "Editar Producto") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL de Imagen") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = sourceUrl,
                onValueChange = { sourceUrl = it },
                label = { Text("URL de la Fuente") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = currency,
                onValueChange = { currency = it },
                label = { Text("Moneda") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = source,
                onValueChange = { source = it },
                label = { Text("Fuente") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val request = ProductRequest(
                        name = name,
                        price = price.toDoubleOrNull(),
                        imageUrl = imageUrl.takeIf { it.isNotBlank() },
                        sourceUrl = sourceUrl.takeIf { it.isNotBlank() },
                        currency = currency,
                        source = source
                    )
                    if (productId == null) {
                        viewModel.createProduct(request)
                    } else {
                        viewModel.updateProduct(productId, request)
                    }
                    onBackClick()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Guardar")
                }
            }
        }
    }
}
