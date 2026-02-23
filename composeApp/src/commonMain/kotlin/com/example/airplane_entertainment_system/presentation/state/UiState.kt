package com.example.airplane_entertainment_system.presentation.state

import com.example.airplane_entertainment_system.domain.models.Flight

sealed class FlightListUiState {
    data object Loading : FlightListUiState()
    data class Success(val flights: List<Flight>) : FlightListUiState()
    data class Error(val message: String) : FlightListUiState()
}

sealed class FlightDetailUiState {
    data object Loading : FlightDetailUiState()
    data class Success(val flight: Flight) : FlightDetailUiState()
    data class Error(val message: String) : FlightDetailUiState()
}
