package com.example.praktikumpemmob4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.praktikumpemmob4.ui.screen.DaftarProductScreen
import com.example.praktikumpemmob4.ui.screen.DetailProductScreen
import com.example.praktikumpemmob4.ui.screen.HubungiKamiScreen
import com.example.praktikumpemmob4.ui.theme.Pert3_listTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Pert3_listTheme(
                darkTheme = true,
                dynamicColor = false
            ) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "daftar_produk"
                ) {
                    composable(route = "daftar_produk") {
                        DaftarProductScreen(navController = navController)
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
                            navController = navController
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
