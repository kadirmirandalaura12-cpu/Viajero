package com.grupo.viajero.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.viajero.model.CambioResponse
import com.grupo.viajero.model.ClimaResponse
import com.grupo.viajero.model.Destino
import com.grupo.viajero.model.Feriado
import com.grupo.viajero.network.RetrofitClient
import kotlinx.coroutines.launch

class ViajeViewModel : ViewModel() {

    // Monedas que sí tiene Frankfurter (datos del Banco Central Europeo)
    val destinos = listOf(
        Destino("Madrid", 40.42, -3.70, "ES", "EUR"),
        Destino("Ciudad de México", 19.43, -99.13, "MX", "MXN"),
        Destino("Río de Janeiro", -22.91, -43.17, "BR", "BRL"),
        Destino("Londres", 51.51, -0.13, "GB", "GBP"),
        Destino("Tokio", 35.68, 139.69, "JP", "JPY")
    )

    var destinoElegido by mutableStateOf(destinos[0])
        private set
    var clima by mutableStateOf<ClimaResponse?>(null)
        private set
    var cambio by mutableStateOf<CambioResponse?>(null)
        private set
    var feriados by mutableStateOf<List<Feriado>>(emptyList())
        private set
    var cargando by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    init {
        elegir(destinos[0])
    }

    fun elegir(destino: Destino) {
        destinoElegido = destino
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                clima = RetrofitClient.clima.obtenerClima(destino.latitud, destino.longitud)
                cambio = RetrofitClient.cambio.obtenerCambio("USD", destino.moneda)
                feriados = RetrofitClient.feriados.obtenerFeriados(2026, destino.codigoPais)
            } catch (e: Exception) {
                error = "Error: ${e.message}"
            }
            cargando = false
        }
    }
}
