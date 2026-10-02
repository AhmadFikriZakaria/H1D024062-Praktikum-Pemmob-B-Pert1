package com.example.praktikumpemmob5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.praktikumpemmob5.ui.screen.DaftarProductScreen
import com.example.praktikumpemmob5.ui.screen.DetailProductScreen
import com.example.praktikumpemmob5.ui.screen.HubungiKamiScreen
import com.example.praktikumpemmob5.ui.theme.Praktikumpemmob5Theme
import com.example.praktikumpemmob5.ui.viewmodel.ProductViewModel

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Praktikumpemmob5Theme(
                darkTheme = false,
                dynamicColor = false
            ) {
                val navController = rememberNavController()
                val productViewModel: ProductViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "daftar_produk"
                ) {
                    composable(route = "daftar_produk") {
                        DaftarProductScreen(
                            navController = navController,
                            viewModel = productViewModel
                        )
                    }

                    composable(
                        route = "detail/{productId}",
                        arguments = listOf(
                            navArgument("productId") {
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val productId =
                            backStackEntry.arguments?.getInt("productId") ?: 0

                        DetailProductScreen(
                            productId = productId,
                            navController = navController,
                            viewModel = productViewModel
                        )
                    }

                    composable(route = "hubungi_kami") {
                        HubungiKamiScreen(navController = navController)
                    }
                }
            }
        }
    }
}
