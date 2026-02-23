package com.example.airplane_entertainment_system.data.remote

import com.example.airplane_entertainment_system.data.mock.MockFlightData
import com.example.airplane_entertainment_system.domain.models.Flight
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    fun createHttpClient(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(json)
            }
        }
    }
}

class MockFlightApiClient : FlightApiService {
    override suspend fun getFlights(): List<Flight> {
        return MockFlightData.getFlights()
    }

    override suspend fun getFlightDetail(id: String): Flight {
        return MockFlightData.getFlightById(id)
            ?: throw Exception("Flight not found")
    }
}


