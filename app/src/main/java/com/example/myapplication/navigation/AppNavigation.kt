package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.screen.CotizacionScreen
import com.example.myapplication.ui.theme.screen.LoginScreen
import com.example.myapplication.ui.theme.screen.MainScreen
import com.example.myapplication.ui.theme.screen.ResumenScreen
import com.example.myapplication.viewModel.CotizacionViewModel
import com.example.myapplication.viewModel.LoginViewModel
import com.example.myapplication.ui.theme.screen.RecuperarPasswordScreen
import com.example.myapplication.ui.theme.screen.RegistroClienteScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Instanciamos los ViewModels que controlan el estado
    val loginViewModel: LoginViewModel = viewModel()
    val cotizacionViewModel: CotizacionViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Pantalla 1: Login
        composable(route = "login") {
            LoginScreen(navController = navController, viewModel = loginViewModel)
        }

        // Pantalla 1.5: Recuperar contraseña
        composable(route = "recuperar") {
            RecuperarPasswordScreen(navController = navController)
        }


        // Pantalla 2: Panel Principal
        composable(route = "main") {
            MainScreen(navController = navController, loginViewModel = loginViewModel, cotizacionViewModel = cotizacionViewModel)
        }

        // Pantalla 2.5: Registro de Clientes
        composable(route = "registro_clientes") {
            RegistroClienteScreen(
                navController = navController,
                cotizacionViewModel = cotizacionViewModel,
                loginViewModel = loginViewModel
            )
        }


        // Pantalla 3: Formulario de Cotización
        composable(route = "cotizacion") {
            CotizacionScreen(
                navController = navController,
                cotizacionViewModel = cotizacionViewModel,
                loginViewModel = loginViewModel
            )
        }

        // Pantalla 4: Resumen y Confirmación
        composable(route = "resumen") {
            ResumenScreen(navController = navController, viewModel = cotizacionViewModel, loginViewModel = loginViewModel)
        }
    }
}