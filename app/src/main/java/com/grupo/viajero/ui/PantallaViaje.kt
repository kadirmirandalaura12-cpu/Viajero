package com.grupo.viajero.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PantallaViaje(vm: ViajeViewModel = viewModel()) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Viajero ✈️", style = MaterialTheme.typography.headlineMedium)
        }

        // Selector de destino
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.destinos) { d ->
                    FilterChip(
                        selected = d == vm.destinoElegido,
                        onClick = { vm.elegir(d) },
                        label = { Text(d.ciudad) }
                    )
                }
            }
        }

        if (vm.cargando) item { CircularProgressIndicator() }
        vm.error?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }

        // 1) Clima
        vm.clima?.let { c ->
            item {
                Tarjeta("🌤️ Clima") {
                    Text("Ahora: ${c.current.temperatura} °C, viento ${c.current.viento} km/h")
                    Text("Hoy: máx ${c.daily.maximas.firstOrNull()} °C / mín ${c.daily.minimas.firstOrNull()} °C")
                }
            }
        }

        // 2) Tipo de cambio
        vm.cambio?.let { t ->
            item {
                Tarjeta("💱 Tipo de cambio") {
                    val valor = t.rates[vm.destinoElegido.moneda]
                    Text("1 USD = $valor ${vm.destinoElegido.moneda}")
                    Text("Fecha: ${t.date}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        // 3) Feriados
        if (vm.feriados.isNotEmpty()) {
            item { Text("📅 Feriados 2026", style = MaterialTheme.typography.titleMedium) }
            items(vm.feriados) { f ->
                Text("${f.date} — ${f.localName}")
            }
        }
    }
}

@Composable
private fun Tarjeta(titulo: String, contenido: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            contenido()
        }
    }
}
