package com.example.myapplication.ui.theme.screen
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.viewModel.CotizacionViewModel
import com.example.myapplication.viewModel.LoginViewModel
@Composable
fun RegistroClienteScreen (
    navController: NavController,
    cotizacionViewModel: CotizacionViewModel,
    loginViewModel: LoginViewModel
) {
    val estadoCotizacion by cotizacionViewModel.uiState.collectAsState()
    val estadoLogin by loginViewModel.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título principal
        Text(
            text = "Trato Echo APP",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón Volver
        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.width(90.dp).height(36.dp)
        ) {
            Text(text = "Volver", fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Usuario activo
        Text(
            text = "${estadoLogin.tenantId.ifBlank { "Nombre usuario" }}",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Registros de Clientes",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Cuadro con la lista de clientes registrados
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            if (estadoCotizacion.items.isEmpty()) {
                Text(
                    text = "No hay clientes ni cotizaciones registradas...",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            } else {
                LazyColumn {
                    items(estadoCotizacion.items) { items ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xF8F9FA))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "-----------------------------------------------------",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Cliente: ${items.NombreCliente}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "Producto: ${items.descripcion}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Monto: \$${items.precio}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Cantidad: ${items.cantidad}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "-----------------------------------------------------",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
