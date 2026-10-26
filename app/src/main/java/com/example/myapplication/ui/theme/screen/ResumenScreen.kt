package com.example.myapplication.ui.theme.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.viewModel.CotizacionViewModel
import com.example.myapplication.viewModel.LoginViewModel

@Composable
fun ResumenScreen(
    navController: NavController,
    viewModel: CotizacionViewModel,
    loginViewModel: LoginViewModel
) {
    //Creamos una variable para
    val estadoLogin by loginViewModel.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Trato Echo APP",
                fontSize = 22.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Nombre de usuario
            Text(
                text = "${estadoLogin.tenantId.ifBlank { "Nombre usuario" }}",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Cuadro central del boceto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Se ha realizado correctamente la cotizacion",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Botón central Nueva cotización
            Button(
                onClick = { navController.navigate("main") },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(text = "Volver a la pagina principal", fontSize = 16.sp)
            }
        }

    }
}