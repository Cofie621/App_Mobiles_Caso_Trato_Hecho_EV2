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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun CotizacionScreen(
    navController: NavController,
    cotizacionViewModel: CotizacionViewModel,
    loginViewModel: LoginViewModel // Inyectamos LoginViewModel para obtener el nombre activo
) {
    // Escuchamos el estado de la cotización y del usuario reactivamente
    val estadoCotizacion by cotizacionViewModel.uiState.collectAsState()
    val estadoLogin by loginViewModel.estado.collectAsState()

    // Variables locales para capturar el producto y precio ingresados
    var productoInput by remember { mutableStateOf("") }
    var precioInput by remember { mutableStateOf("") }

    //Variable para tomar el nombre del cliente
    var nombreCliente by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
            // Encabezado con título
            Text(
                text = "Nueva Cotizacion",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Botón Volver
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.width(90.dp).height(36.dp)
            ) {
                Text(text = "Volver", fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Nombre de usuario dinámico traído desde LoginViewModel
            Text(
                text = "${estadoLogin.tenantId.ifBlank { "Nombre usuario" }}",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // CONFIGURACIÓN DE MONEDA (CLP, UF, USD)
            Text(text = "CONFIGURACIÓN DE MONEDA", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Moneda:", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(16.dp))
                listOf("CLP", "UF", "USD").forEach { moneda ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        RadioButton(
                            selected = estadoCotizacion.moneda == moneda,
                            onClick = { cotizacionViewModel.cambiarMoneda(moneda) }
                        )
                        Text(text = moneda, fontSize = 14.sp)
                    }
                }
            }


            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ÍTEMS / PRODUCTOS", fontSize = 12.sp, fontWeight = FontWeight.Bold)

            // Formulario para ingresar el nombre del producto y su precio
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = productoInput,
                    onValueChange = { productoInput = it },
                    placeholder = { Text("Producto") },
                    modifier = Modifier.weight(2f)
                )
                OutlinedTextField(
                    value = precioInput,
                    onValueChange = { precioInput = it },
                    placeholder = { Text("Precio") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            //Formulario para ingresar el nombre del cliente
            Text(text = "Nombre", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = nombreCliente,
                onValueChange = {nombreCliente = it},
                label = {Text("Nombre del Cliente")},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Botón para agregar el ítem a la cotización
            Button(
                onClick = {
                    val precio = precioInput.toDoubleOrNull() ?: 0.0
                    if (productoInput.isNotBlank() && precio > 0.0) {
                        cotizacionViewModel.agregarProducto(productoInput, precio, 1, nombreCliente)
                        productoInput = ""
                        precioInput = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar Producto", fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cuadro contenedor de la lista de productos
            Text(text = "Tabla de cotizaciones recientes", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                LazyColumn {
                    items(estadoCotizacion.items) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${item.cantidad} ----- ${item.descripcion} ----- ($${item.precio}), Cliente: ${item.NombreCliente}",
                                fontSize = 11.sp
                            )
                            TextButton(
                                onClick = { cotizacionViewModel.eliminarProducto(item.descripcion) },
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.height(24.dp)
                            ) {
                                Text("X", fontSize = 12.sp, color = Color.Red, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // BLOQUE DE RESUMEN Y TOTALES
            Text(
                text = "RESUMEN Y TOTALES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Text(text = "Subtotal: \$${cotizacionViewModel.calcularSubtotal()}", fontSize = 12.sp)
                    Text(text = "Descuento Aplicado (%): 0%", fontSize = 12.sp)
                    Text(text = "Impuesto: \$${cotizacionViewModel.calcularImpuesto()}", fontSize = 12.sp)
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = "TOTAL FINAL: \$${cotizacionViewModel.calcularTotalFinal()} ${estadoCotizacion.moneda}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón final de envío
        Button(
            onClick = { navController.navigate("resumen") },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(text = "FINALIZAR Y ENVIAR AL CLIENTE", fontSize = 13.sp)
        }
    }
}