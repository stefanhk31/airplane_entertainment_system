package com.example.airplane_entertainment_system.data.remote

import com.example.airplane_entertainment_system.domain.models.Flight

interface FlightApiService {
    suspend fun getFlights(): List<Flight>
    suspend fun getFlightDetail(id: String): Flight
}

