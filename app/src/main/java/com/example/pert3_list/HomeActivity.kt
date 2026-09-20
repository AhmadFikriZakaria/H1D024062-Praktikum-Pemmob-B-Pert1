package com.example.pert3_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pert3_list.ui.screen.DaftarProductScreen
import com.example.pert3_list.ui.theme.Pert3_listTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Pert3_listTheme {
                DaftarProductScreen()
            }
        }
    }
}
