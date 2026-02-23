package com.example.airplane_entertainment_system.viewmodels

import com.example.airplane_entertainment_system.data.repositories.FlightRepository
import com.example.airplane_entertainment_system.domain.models.Flight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockFlightRepository(
    private val flights: List<Flight> = emptyList(),
    private val shouldFail: Boolean = false
) : FlightRepository {

    override fun getFlights(): Flow<Result<List<Flight>>> = flowOf(
        if (shouldFail) {
            Result.failure(Exception("Test error"))
        } else {
            Result.success(flights)
        }
    )

    override fun getFlightDetail(id: String): Flow<Result<Flight>> = flowOf(
        flights.find { it.id == id }?.let { flight ->
            Result.success(flight)
        } ?: Result.failure(Exception("Flight not found"))
    )
}

