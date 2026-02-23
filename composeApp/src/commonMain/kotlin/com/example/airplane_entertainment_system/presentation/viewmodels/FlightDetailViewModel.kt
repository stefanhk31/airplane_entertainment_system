package com.example.airplane_entertainment_system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airplane_entertainment_system.data.repositories.FlightRepository
import com.example.airplane_entertainment_system.presentation.state.FlightDetailUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FlightDetailViewModel(
    private val repository: FlightRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FlightDetailUiState>(FlightDetailUiState.Loading)
    val uiState: StateFlow<FlightDetailUiState> = _uiState.asStateFlow()

    fun loadFlightDetail(flightId: String) {
        _uiState.value = FlightDetailUiState.Loading
        viewModelScope.launch {
            repository.getFlightDetail(flightId).collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { flight -> FlightDetailUiState.Success(flight) },
                    onFailure = { error -> FlightDetailUiState.Error(error.message ?: "Unknown error") }
                )
            }
        }
    }
}
