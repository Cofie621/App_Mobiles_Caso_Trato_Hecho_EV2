package com.example.myapplication.ui.theme.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.viewModel.LoginViewModel
import com.example.myapplication.viewModel.CotizacionViewModel
import androidx.compose.foundation.lazy.items

@Composable
fun MainScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    cotizacionViewModel: CotizacionViewModel
) {
    val loginEstado by loginViewModel.estado.collectAsState()
    var buscador by remember { mutableStateOf("") }
    val estadoCotizacion by cotizacionViewModel.uiState.collectAsState()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Título superior del boceto
            Text(
                text = "Trato Echo APP",
                fontSize = 22.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Volver
            OutlinedButton(
                onClick = { navController.navigate("login") },
                modifier = Modifier.width(90.dp).height(36.dp)
            ) {
                Text(text = "Volver", fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))


            // Nombre de usuario
            Text(
                text = "Bienvenido ${loginEstado.tenantId.ifBlank { "Usuario" }}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón Nueva cotización destacado
            Button(
                onClick = { navController.navigate("cotizacion") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Nueva Cotización", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sección Cotizaciones Recientes y Caja contenedora
            Text(
                text = "Cotizaciones Recientes",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ){
                if (estadoCotizacion.items.isEmpty()){
                   Text(
                       text = "cotizaciones recientes realizadas",
                       fontSize = 12.sp,
                       color = Color.Gray
                   )
                } else {
                    LazyColumn{
                        items(estadoCotizacion.items){ items ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Text(
                                    text = "Producto: ${items.descripcion} || Pago: \$${items.precio} || Cliente: ${items.NombreCliente}"
                                    , fontSize = 12.sp
                                )
                            }

                        }
                    }
                }
            }
        }


        // Botón inferior Registros Clientes
        OutlinedButton(
            onClick = { navController.navigate("registro_clientes")},
            modifier = Modifier
                .align(Alignment.End)
                .width(130.dp)
        ) {
            Text(text = "Registros Clientes", fontSize = 12.sp)
        }
    }
}