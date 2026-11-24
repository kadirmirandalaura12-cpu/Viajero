package com.grupo.viajero.network

import com.grupo.viajero.model.CambioResponse
import com.grupo.viajero.model.ClimaResponse
import com.grupo.viajero.model.Feriado
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Cada API tiene su propia URL base, por eso hay 3 interfaces.

interface ClimaApi {
    @GET("v1/forecast")
    suspend fun obtenerClima(
        @Query("latitude") latitud: Double,
        @Query("longitude") longitud: Double,
        @Query("current") actual: String = "temperature_2m,wind_speed_10m",
        @Query("daily") diario: String = "temperature_2m_max,temperature_2m_min",
        @Query("timezone") zona: String = "auto"
    ): ClimaResponse
}

interface CambioApi {
    @GET("latest")
    suspend fun obtenerCambio(
        @Query("from") desde: String,
        @Query("to") hacia: String
    ): CambioResponse
}

interface FeriadosApi {
    @GET("api/v3/PublicHolidays/{anio}/{pais}")
    suspend fun obtenerFeriados(
        @Path("anio") anio: Int,
        @Path("pais") pais: String
    ): List<Feriado>
}

object RetrofitClient {
    private fun crear(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val clima: ClimaApi = crear("https://api.open-meteo.com/").create(ClimaApi::class.java)
    val cambio: CambioApi = crear("https://api.frankfurter.app/").create(CambioApi::class.java)
    val feriados: FeriadosApi = crear("https://date.nager.at/").create(FeriadosApi::class.java)
}
