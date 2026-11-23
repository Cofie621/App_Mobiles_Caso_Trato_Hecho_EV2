package com.example.myapplication.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.viewModel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val estado by viewModel.estado.collectAsState()
    var recordar by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        // Título exacto del boceto
        Text(
            text = "Trato Echo APP",
            fontSize = 24.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )


        Spacer(modifier = Modifier.height(16.dp))

        // Campo Correo
        Text(text = "Correo", fontSize = 14.sp)
        OutlinedTextField(
            value = estado.correo,
            onValueChange = viewModel::onCorreoChange,
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errores.correo != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo nombre
        Text(text = "Nombre", fontSize = 14.sp)
        OutlinedTextField(
            value = estado.tenantId,
            onValueChange = viewModel::onTenantIdChange,
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errores.tenantId != null
        )
        Spacer(modifier = Modifier.height(16.dp))


        // Campo Contraseña
        Text(text = "Contraseña", fontSize = 14.sp)
        OutlinedTextField(
            value = estado.clave,
            onValueChange = viewModel::onClaveChange,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errores.clave != null
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Fila con Recordar y ¿Olvidaste tu contraseña?
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = recordar, onCheckedChange = { recordar = it })
                Text(text = "Recordar", fontSize = 14.sp)
            }
            TextButton(
                onClick = {
                    // Redirige a la pantalla de recuperación
                    navController.navigate("recuperar")
                }
            ){
                Text(text = "¿Olvidaste tu contraseña?")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Iniciar Sesión
        Button(
            onClick = {
                if (viewModel.validarLogin()) {
                    navController.navigate("main")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Iniciar Sesion", fontSize = 16.sp)
        }
    }
}