package com.example.airplane_entertainment_system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airplane_entertainment_system.data.repositories.FlightRepository
import com.example.airplane_entertainment_system.presentation.state.FlightListUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FlightListViewModel(
    private val repository: FlightRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FlightListUiState>(FlightListUiState.Loading)
    val uiState: StateFlow<FlightListUiState> = _uiState.asStateFlow()

    init {
        loadFlights()
    }

    private fun loadFlights() {
        viewModelScope.launch {
            repository.getFlights().collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { flights -> FlightListUiState.Success(flights) },
                    onFailure = { error -> FlightListUiState.Error(error.message ?: "Unknown error") }
                )
            }
        }
    }

    fun refresh() {
        _uiState.value = FlightListUiState.Loading
        loadFlights()
    }
}
