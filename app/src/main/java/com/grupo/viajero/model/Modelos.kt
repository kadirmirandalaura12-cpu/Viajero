package com.grupo.viajero.model

import com.google.gson.annotations.SerializedName

// ===================== 1) OPEN-METEO (clima) =====================
// GET https://api.open-meteo.com/v1/forecast?latitude=..&longitude=..&current=temperature_2m,wind_speed_10m&daily=temperature_2m_max,temperature_2m_min&timezone=auto
data class ClimaResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val current: ClimaActual,
    val daily: ClimaDiario
)

data class ClimaActual(
    val time: String,
    @SerializedName("temperature_2m") val temperatura: Double,
    @SerializedName("wind_speed_10m") val viento: Double
)

data class ClimaDiario(
    val time: List<String>,
    @SerializedName("temperature_2m_max") val maximas: List<Double>,
    @SerializedName("temperature_2m_min") val minimas: List<Double>
)

// ===================== 2) FRANKFURTER (tipo de cambio) =====================
// GET https://api.frankfurter.app/latest?from=USD&to=EUR
data class CambioResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Map<String, Double> // "EUR" -> 0.92
)

// ===================== 3) NAGER.DATE (feriados) =====================
// GET https://date.nager.at/api/v3/PublicHolidays/2026/ES   -> devuelve una LISTA
data class Feriado(
    val date: String,
    val localName: String,
    val name: String,
    val countryCode: String,
    val fixed: Boolean,
    val global: Boolean,
    val counties: List<String>?,
    val launchYear: Int?,
    val types: List<String>
)

// ===================== Clase propia de la app (no viene de ninguna API) =====================
data class Destino(
    val ciudad: String,
    val latitud: Double,
    val longitud: Double,
    val codigoPais: String, // para Nager.Date
    val moneda: String      // para Frankfurter
)
