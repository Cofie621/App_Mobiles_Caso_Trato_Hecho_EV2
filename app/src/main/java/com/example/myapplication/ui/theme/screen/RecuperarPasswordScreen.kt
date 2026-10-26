package com.example.myapplication.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RecuperarPasswordScreen(navController: NavController){
    //Variables para capturar las contraseñas
    var nuevaContrasenia by remember { mutableStateOf("") }
    var repetirContrasenia by remember { mutableStateOf("") }
    var mensajeConfirmacion by remember { mutableStateOf(false) }

    //Hago el Column para organizar la informacion de forma vertical
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        //Se agrega el titulo de la app
        Text(
            text = "Trato Echo APP",
            fontSize = 24.sp,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        //Se agrega el texto para restablecer la contraseña
        Text(
            text = "Restablecer Contraseña",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo: Nueva contraseña
        OutlinedTextField(
            value = nuevaContrasenia,
            onValueChange = { nuevaContrasenia = it },
            label = { Text("Nueva Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo: Ingrese nuevamente la contraseña
        OutlinedTextField(
            value = repetirContrasenia,
            onValueChange = { repetirContrasenia = it },
            label = { Text("Ingrese nuevamente la contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de guardar / cambiar
        Button(
            onClick = {
                if (nuevaContrasenia.isNotBlank() && nuevaContrasenia == repetirContrasenia) {
                    mensajeConfirmacion = true
                }
            },
            modifier = Modifier
                .fillMaxWidth().height(48.dp)
        ){
            Text("Cambiar Contraseña")
        }

        if (mensajeConfirmacion) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Contraseña cambiada exitosamente",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón para volver al inicio (Login)
        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Volver al inicio")
        }

    }
}