package com.example.miaumarket.wear.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.example.miaumarket.wear.ui.screens.catalog.CatalogScreen
import com.example.miaumarket.wear.ui.screens.product.ProductDetailScreen

sealed class WearRoute(val route: String) {
    object Catalog : WearRoute("catalog")
    object ProductDetail : WearRoute("product/{productId}") {
        fun createRoute(productId: Long) = "product/$productId"
    }
}

@Composable
fun WearNavGraph(navController: NavHostController) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = WearRoute.Catalog.route
    ) {
        composable(WearRoute.Catalog.route) {
            CatalogScreen(
                onProductClick = { productId ->
                    navController.navigate(WearRoute.ProductDetail.createRoute(productId))
                }
            )
        }

        composable(WearRoute.ProductDetail.route) {
            ProductDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

