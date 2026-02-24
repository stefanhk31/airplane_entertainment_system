package com.example.airplane_entertainment_system

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.airplane_entertainment_system.presentation.screens.FlightDetailScreen
import com.example.airplane_entertainment_system.presentation.screens.FlightListScreen

@Composable
actual fun AppContent() {
    // iOS implementation: simple state-based navigation without NavController
    val currentScreen = remember { mutableStateOf<String?>(null) }

    when (currentScreen.value) {
        null -> {
            FlightListScreen(
                onFlightSelected = { flightId ->
                    currentScreen.value = flightId
                }
            )
        }
        else -> {
            val flightId = currentScreen.value ?: ""
            FlightDetailScreen(
                flightId = flightId,
                onNavigateBack = {
                    currentScreen.value = null
                }
            )
        }
    }
}

