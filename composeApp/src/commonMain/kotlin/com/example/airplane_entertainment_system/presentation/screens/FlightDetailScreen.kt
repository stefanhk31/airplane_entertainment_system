package com.example.airplane_entertainment_system.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.airplane_entertainment_system.data.remote.FlightApiService
import com.example.airplane_entertainment_system.data.remote.MockFlightApiClient
import com.example.airplane_entertainment_system.data.repositories.FlightRepositoryImpl
import com.example.airplane_entertainment_system.presentation.components.RouteInfo
import com.example.airplane_entertainment_system.presentation.state.FlightDetailUiState
import com.example.airplane_entertainment_system.presentation.viewmodels.FlightDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightDetailScreen(
    flightId: String,
    onNavigateBack: () -> Unit,
    viewModel: FlightDetailViewModel = viewModel {
        val apiClient: FlightApiService = MockFlightApiClient()
        val repository = FlightRepositoryImpl(apiClient)
        FlightDetailViewModel(repository)
    }
) {
    remember {
        viewModel.loadFlightDetail(flightId)
        0
    }

    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Flight Details",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
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
                is FlightDetailUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is FlightDetailUiState.Success -> {
                    val flight = state.flight
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Flight number and status
                        Text(
                            text = flight.flightNumber,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Route
                        RouteInfo(
                            departure = flight.departure,
                            arrival = flight.arrival,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        // Details
                        DetailRow("Aircraft:", flight.aircraft)
                        DetailRow("Gate:", flight.gate ?: "N/A")
                        DetailRow("Seat:", flight.seat ?: "N/A")
                        DetailRow("Class:", flight.seatClass.toString())
                        DetailRow("Status:", flight.boardingStatus.toString())
                    }
                }
                is FlightDetailUiState.Error -> {
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
fun DetailRow(label: String, value: String) {
    Column(
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}





