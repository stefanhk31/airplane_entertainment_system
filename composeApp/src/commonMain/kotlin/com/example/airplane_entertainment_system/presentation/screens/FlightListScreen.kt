package com.example.airplane_entertainment_system.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.airplane_entertainment_system.data.remote.FlightApiService
import com.example.airplane_entertainment_system.data.remote.MockFlightApiClient
import com.example.airplane_entertainment_system.data.repositories.FlightRepositoryImpl
import com.example.airplane_entertainment_system.presentation.components.FlightCard
import com.example.airplane_entertainment_system.presentation.state.FlightListUiState
import com.example.airplane_entertainment_system.presentation.viewmodels.FlightListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightListScreen(
    onFlightSelected: (String) -> Unit,
    viewModel: FlightListViewModel = viewModel {
        val apiClient: FlightApiService = MockFlightApiClient()
        val repository = FlightRepositoryImpl(apiClient)
        FlightListViewModel(repository)
    }
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Flights",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState.value) {
                is FlightListUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is FlightListUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.flights) { flight ->
                            FlightCard(
                                flight = flight,
                                onClick = { onFlightSelected(flight.id) }
                            )
                        }
                    }
                }
                is FlightListUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${state.message}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FlightListScreenPreview() {
    FlightListScreen(onFlightSelected = {})
}



