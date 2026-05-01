package com.example.miaumarket.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.miaumarket.wear.ui.navigation.WearNavGraph
import com.example.miaumarket.wear.ui.theme.MiauMarketWearTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiauMarketWearTheme {
                val navController = rememberSwipeDismissableNavController()
                WearNavGraph(navController = navController)
            }
        }
    }
}

