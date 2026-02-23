package com.example.airplane_entertainment_system.data.repositories

import com.example.airplane_entertainment_system.domain.models.Flight
import com.example.airplane_entertainment_system.data.remote.FlightApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface FlightRepository {
    fun getFlights(): Flow<Result<List<Flight>>>
    fun getFlightDetail(id: String): Flow<Result<Flight>>
}

class FlightRepositoryImpl(
    private val apiService: FlightApiService
) : FlightRepository {

    override fun getFlights(): Flow<Result<List<Flight>>> = flow {
        try {
            val flights = apiService.getFlights()
            emit(Result.success(flights))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getFlightDetail(id: String): Flow<Result<Flight>> = flow {
        try {
            val flight = apiService.getFlightDetail(id)
            emit(Result.success(flight))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

