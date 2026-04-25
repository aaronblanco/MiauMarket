package com.example.miaumarket.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.miaumarket.ui.screens.catalog.*
import com.example.miaumarket.ui.screens.cart.CartScreen
import com.example.miaumarket.ui.screens.cart.CartViewModel
import com.example.miaumarket.ui.screens.login.LoginScreen
import com.example.miaumarket.ui.screens.login.LoginViewModel
import com.example.miaumarket.ui.screens.register.RegisterScreen
import com.example.miaumarket.ui.screens.register.RegisterViewModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object RegisterRoute

@Serializable
object CatalogRoute

@Serializable
data class ProductDetailRoute(val id: Long)

@Serializable
data class ProductFormRoute(val productId: Long? = null)

@Serializable
object CartRoute

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Any
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<LoginRoute> {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(CatalogRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(RegisterRoute)
                }
            )
        }
        composable<RegisterRoute> {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.navigate(CatalogRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(LoginRoute) {
                        popUpTo(RegisterRoute) { inclusive = true }
                    }
                }
            )
        }
        composable<CatalogRoute> {
            val viewModel: ProductViewModel = hiltViewModel()
            CatalogScreen(
                viewModel = viewModel,
                onProductClick = { id ->
                    navController.navigate(ProductDetailRoute(id))
                },
                onNavigateToLogin = {
                    navController.navigate(LoginRoute)
                },
                onNavigateToCreateProduct = {
                    navController.navigate(ProductFormRoute())
                },
                onNavigateToCart = {
                    navController.navigate(CartRoute)
                }
            )
        }
        composable<ProductDetailRoute> { backStackEntry ->
            val route: ProductDetailRoute = backStackEntry.toRoute()
            // Obtenemos el ViewModel de la entrada del Catálogo para compartir la instancia
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(CatalogRoute)
            }
            val viewModel: ProductViewModel = hiltViewModel(parentEntry)
            
            ProductDetailScreen(
                productId = route.id,
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { id ->
                    navController.navigate(ProductFormRoute(id))
                }
            )
        }
        composable<ProductFormRoute> { backStackEntry ->
            val route: ProductFormRoute = backStackEntry.toRoute()
            // Compartimos la misma instancia también aquí
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(CatalogRoute)
            }
            val viewModel: ProductViewModel = hiltViewModel(parentEntry)

            ProductFormScreen(
                productId = route.productId,
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable<CartRoute> {
            val viewModel: CartViewModel = hiltViewModel()
            CartScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
