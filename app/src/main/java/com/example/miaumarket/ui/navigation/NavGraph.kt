package com.example.miaumarket.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.miaumarket.ui.screens.catalog.*
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
data class ProductDetailRoute(val id: String)

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
                }
            )
        }
        composable<ProductDetailRoute> { backStackEntry ->
            val route: ProductDetailRoute = backStackEntry.toRoute()
            val viewModel: ProductViewModel = hiltViewModel()
            ProductDetailScreen(
                productId = route.id,
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
