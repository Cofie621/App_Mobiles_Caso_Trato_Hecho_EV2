package com.example.myapplication.viewModel

import androidx.lifecycle.ViewModel
import com.example.myapplication.model.CotizacionEstado
import com.example.myapplication.model.CotizacionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.myapplication.AlmacenamientoLocal
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class CotizacionViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val _uiState = MutableStateFlow(
        CotizacionEstado(items = AlmacenamientoLocal.obtenerCotizaciones(context))
    )
    val uiState: StateFlow<CotizacionEstado> = _uiState.asStateFlow()

    fun cambiarMoneda(nuevaMoneda: String) {
        _uiState.update { it.copy(moneda = nuevaMoneda) }
    }

    fun agregarProducto(descripcion: String, precio: Double, cantidad: Int = 1, NombreCliente: String) {
        val nuevoItem = CotizacionItem(descripcion, precio, cantidad, NombreCliente)

        _uiState.update { currentState ->
            val listaActualizada = currentState.items + nuevoItem

            // Aquí guardamos localmente
            AlmacenamientoLocal.guardarCotizaciones(context, listaActualizada)

            currentState.copy(items = listaActualizada)
        }
    }

    fun calcularSubtotal(): Double {
        return _uiState.value.items.sumOf { it.precio * it.cantidad }
    }

    fun calcularImpuesto(): Double {
        return calcularSubtotal() * 0.19
    }

    fun calcularTotalFinal(): Double {
        return calcularSubtotal() + calcularImpuesto()
    }

    fun eliminarProducto(descripcion: String) {
        _uiState.update { estadoActual ->
            val nuevaLista = estadoActual.items.filterNot { it.descripcion == descripcion }

            // Guardamos la lista actualizada en el almacenamiento local
            AlmacenamientoLocal.guardarCotizaciones(context, nuevaLista)

            estadoActual.copy(items = nuevaLista)
        }
    }
}
