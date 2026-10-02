package com.example.praktikumpemmob5.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.praktikumpemmob5.R
import com.example.praktikumpemmob5.data.model.Product
import com.example.praktikumpemmob5.ui.theme.Praktikumpemmob5Theme
import com.example.praktikumpemmob5.ui.viewmodel.ProductUiState
import com.example.praktikumpemmob5.ui.viewmodel.ProductViewModel
import com.example.praktikumpemmob5.util.JualanConstants.BASE_URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProductScreen(productId: Int, navController: NavController, viewModel: ProductViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var quantity by rememberSaveable { mutableStateOf(1) }

    when (val state = uiState) {
        ProductUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ProductUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: ${state.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is ProductUiState.Success -> {
            val product = state.products.find { it.id == productId }

            if (product == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Produk tidak ditemukan.")
                }
            } else {
                StatelessDetailProduct(
                    product = product,
                    quantity = quantity,
                    onQuantityChange = { newQuantity ->
                        quantity = newQuantity
                    },
                    onBackClick = { navController.popBackStack() },
                    onAddToCartClick = {
                        Toast.makeText(
                            context,
                            "Membeli sebanyak $quantity",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessDetailProduct(
    product: Product?,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Produk") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_icon),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (product != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                val imageModel: Any =
                    if (product.img == "dummy_product") {
                        R.drawable.dummy_product
                    } else {
                        "${BASE_URL}img/${product.img}"
                    }

                AsyncImage(
                    model = imageModel,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(size = 8.dp))
                        .background(Color.White),
                    contentScale = ContentScale.Fit
                )

                Column(modifier = Modifier.padding(all = 16.dp)) {
                    product.category?.let { category ->
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF4A4458),
                            modifier = Modifier
                                .clip(RoundedCornerShape(size = 4.dp))
                                .background(Color(0xFFEADDFF))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Text(
                        product.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Rp ${product.price}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Deskripsi", fontWeight = FontWeight.Bold)
                    Text(product.description ?: "")
                    Text("Stok Tersedia: ${product.stock}")

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Jumlah Beli")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FilledTonalIconButton(
                                onClick = {
                                    if (quantity > 1) onQuantityChange(quantity - 1)
                                },
                                enabled = quantity > 1
                            ) {
                                Text("-")
                            }

                            Text(
                                quantity.toString(),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            FilledTonalIconButton(
                                onClick = {
                                    if (quantity < product.stock) onQuantityChange(quantity + 1)
                                },
                                enabled = quantity < product.stock
                            ) {
                                Text("+")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onAddToCartClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = product.stock > 0 && quantity > 0
                    ) {
                        Text("Tambah ke Keranjang")
                    }
                }
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun StatelessDetailProductPreview() {
    Praktikumpemmob5Theme {
        StatelessDetailProduct(
            product = DummyData.products.firstOrNull(),
            isLoading = false,
            quantity = 5,
            onQuantityChange = {},
            onBackClick = {},
            onAddToCartClick = {}
        )
    }
}
*/
