package com.example.miaumarket.ui.screens.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.miaumarket.R
import com.example.miaumarket.data.remote.dto.ProductResponse
import com.example.miaumarket.ui.theme.CartActionColor
import com.example.miaumarket.ui.theme.MiauMarketTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: ProductViewModel,
    onProductClick: (Long) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToCreateProduct: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val products by viewModel.products.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val isAdmin by viewModel.isAdmin.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    CatalogContent(
        products = products,
        isLoading = isLoading,
        searchQuery = searchQuery,
        error = error,
        isLoggedIn = isLoggedIn,
        isAdmin = isAdmin,
        snackbarHostState = snackbarHostState,
        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
        onProductClick = onProductClick,
        onAddToCart = { product ->
            viewModel.addToCart(product)
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "${product.name} añadido al carrito",
                    duration = SnackbarDuration.Short
                )
            }
        },
        onNavigateToLogin = onNavigateToLogin,
        onLogout = { viewModel.logout() },
        onLoadNextPage = viewModel::loadNextPage,
        onNavigateToCreateProduct = onNavigateToCreateProduct,
        onNavigateToCart = onNavigateToCart
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogContent(
    products: List<ProductResponse>,
    isLoading: Boolean,
    searchQuery: String,
    error: String?,
    isLoggedIn: Boolean,
    isAdmin: Boolean,
    snackbarHostState: SnackbarHostState,
    onSearchQueryChange: (String) -> Unit,
    onProductClick: (Long) -> Unit,
    onAddToCart: (ProductResponse) -> Unit,
    onNavigateToLogin: () -> Unit,
    onLogout: () -> Unit,
    onLoadNextPage: () -> Unit,
    onNavigateToCreateProduct: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val gridState = rememberLazyGridState()
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(gridState, products.size, isLoading) {
        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = layoutInfo.totalItemsCount
            totalItems > 0 && lastVisible >= totalItems - 4
        }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                onLoadNextPage()
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToCart) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrito"
                        )
                    }
                    Box {
                        IconButton(onClick = { 
                            if (isLoggedIn) showMenu = true else onNavigateToLogin() 
                        }) {
                            Icon(
                                imageVector = if (isLoggedIn) Icons.Default.Person else Icons.Default.AccountCircle,
                                contentDescription = stringResource(R.string.login_icon_cd),
                                tint = if (isLoggedIn) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Cerrar sesión") },
                                onClick = {
                                    showMenu = false
                                    onLogout()
                                }
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(onClick = onNavigateToCreateProduct) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir producto")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchQuery,
                        onQueryChange = onSearchQueryChange,
                        onSearch = { },
                        expanded = false,
                        onExpandedChange = { },
                        placeholder = { Text(stringResource(R.string.search_placeholder)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                    )
                },
                expanded = false,
                onExpandedChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {}

            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (products.isEmpty() && isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (products.isEmpty()) {
                    val emptyMessage = if (searchQuery.isBlank()) {
                        stringResource(R.string.no_products_available)
                    } else {
                        stringResource(R.string.no_products_found_for_query, searchQuery)
                    }
                    Text(
                        text = emptyMessage,
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = gridState,
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(products) { product ->
                            ProductCard(
                                product = product,
                                onClick = { onProductClick(product.id) },
                                onAddToCart = { onAddToCart(product) }
                            )
                        }
                        if (isLoading) {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ProductResponse,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.price?.let { "$it ${product.currency}" } ?: "-",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (product.source != null) {
                        SuggestionChip(
                            onClick = { },
                            label = { Text(product.source, style = MaterialTheme.typography.labelSmall) }
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    IconButton(
                        onClick = onAddToCart,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = "Añadir al carrito",
                            tint = CartActionColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogScreenPreview() {
    MiauMarketTheme {
        CatalogContent(
            products = listOf(
                ProductResponse(id = 1L, name = "Cat Tree", price = 49.99, currency = "EUR", source = "manual"),
                ProductResponse(id = 2L, name = "Cat Toy", price = 5.99, currency = "EUR", source = "kiwoko")
            ),
            isLoading = false,
            searchQuery = "",
            error = null,
            isLoggedIn = true,
            isAdmin = true,
            snackbarHostState = remember { SnackbarHostState() },
            onSearchQueryChange = {},
            onProductClick = {},
            onAddToCart = {},
            onNavigateToLogin = {},
            onLogout = {},
            onLoadNextPage = {},
            onNavigateToCreateProduct = {},
            onNavigateToCart = {}
        )
    }
}
