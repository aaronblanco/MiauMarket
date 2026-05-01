package com.example.miaumarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.miaumarket.core.data.domain.repository.AuthRepository
import com.example.miaumarket.ui.navigation.CatalogRoute
import com.example.miaumarket.ui.navigation.LoginRoute
import com.example.miaumarket.ui.navigation.NavGraph
import com.example.miaumarket.ui.theme.MiauMarketTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiauMarketTheme {
                val navController = rememberNavController()
                
                // Always start at Catalog now, login is optional
                NavGraph(
                    navController = navController,
                    startDestination = CatalogRoute
                )
            }
        }
    }
}
