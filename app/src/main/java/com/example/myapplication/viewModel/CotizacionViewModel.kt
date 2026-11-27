package com.example.myapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.AlmacenamientoLocal
import com.example.myapplication.model.CotizacionEstado
import com.example.myapplication.model.CotizacionItem
import com.example.myapplication.model.Producto
import com.example.myapplication.repository.CotizacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CotizacionViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val repository = CotizacionRepository(context)

    private val _uiState = MutableStateFlow(
        CotizacionEstado(items = AlmacenamientoLocal.obtenerCotizaciones(context))
    )
    val uiState: StateFlow<CotizacionEstado> = _uiState.asStateFlow()

    // Controla si el trabajador agregó al menos un producto en esta sesión
    private var productoAgregadoEnSesion = false

    init {
        // Apenas se crea el ViewModel, pedimos los productos a la API
        cargarProductos()
    }

    /**
     * Pide a la API la lista de productos (GET api/productos.php)
     * y la deja en el estado para que la pantalla la muestre.
     */
    fun cargarProductos() {
        _uiState.update { it.copy(cargandoProductos = true, errorProductos = null) }

        viewModelScope.launch {
            repository.obtenerProductos()
                .onSuccess { lista ->
                    _uiState.update {
                        it.copy(
                            productos = lista,
                            cargandoProductos = false,
                            errorProductos = if (lista.isEmpty()) "No hay productos en la base de datos" else null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            cargandoProductos = false,
                            errorProductos = "No se pudieron cargar los productos: ${error.message ?: "sin conexión"}"
                        )
                    }
                }
        }
    }

    /**
     * Agrega a la cotización un producto elegido de la lista de la API.
     * El nombre y el precio salen de la base de datos (no se escriben a mano).
     */
    fun agregarProductoDeLista(producto: Producto, cantidad: Int, nombreCliente: String) {
        val nuevoItem = CotizacionItem(
            descripcion = producto.nombre,
            precio = producto.precio,
            cantidad = cantidad.coerceAtLeast(1),
            NombreCliente = nombreCliente,
            productoId = producto.id
        )
        productoAgregadoEnSesion = true

        _uiState.update { currentState ->
            val listaActualizada = currentState.items + nuevoItem
            AlmacenamientoLocal.guardarCotizaciones(context, listaActualizada)
            currentState.copy(items = listaActualizada, mensajeEnvio = null)
        }
    }

    fun cambiarMoneda(nuevaMoneda: String) {
        _uiState.update { it.copy(moneda = nuevaMoneda) }
    }

    fun agregarProducto(descripcion: String, precio: Double, cantidad: Int = 1, NombreCliente: String) {
        val nuevoItem = CotizacionItem(descripcion, precio, cantidad, NombreCliente)
        productoAgregadoEnSesion = true

        _uiState.update { currentState ->
            val listaActualizada = currentState.items + nuevoItem
            AlmacenamientoLocal.guardarCotizaciones(context, listaActualizada)
            currentState.copy(items = listaActualizada, mensajeEnvio = null)
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
            AlmacenamientoLocal.guardarCotizaciones(context, nuevaLista)
            if (nuevaLista.isEmpty()) productoAgregadoEnSesion = false
            estadoActual.copy(items = nuevaLista)
        }
    }

    fun enviarCotizacion(tenantId: String, alTerminar: (exito: Boolean) -> Unit = {}) {
        val estado = _uiState.value

        // Impide finalizar si no agregó ningún producto nuevo en esta pantalla
        if (!productoAgregadoEnSesion || estado.items.isEmpty()) {
            _uiState.update {
                it.copy(mensajeEnvio = "Debes agregar al menos un producto nuevo antes de finalizar")
            }
            return
        }

        _uiState.update { it.copy(enviando = true, mensajeEnvio = null) }

        viewModelScope.launch {
            val resultado = repository.guardarCotizacion(
                tenantId = tenantId,
                moneda = estado.moneda,
                items = estado.items,
                subtotal = calcularSubtotal(),
                impuesto = calcularImpuesto(),
                total = calcularTotalFinal()
            )

            val exito = resultado.isSuccess
            if (exito) {
                productoAgregadoEnSesion = false
            }

            _uiState.update {
                it.copy(
                    enviando = false,
                    mensajeEnvio = if (exito) {
                        "Cotización enviada al servidor"
                    } else {
                        "Error al enviar a la API: ${resultado.exceptionOrNull()?.message ?: "sin conexión"}"
                    }
                )
            }
            alTerminar(exito)
        }
    }

    fun limpiarMensajeEnvio() {
        _uiState.update { it.copy(mensajeEnvio = null) }
    }
}
