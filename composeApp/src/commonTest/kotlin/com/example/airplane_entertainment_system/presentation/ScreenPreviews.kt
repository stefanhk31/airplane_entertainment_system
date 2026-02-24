package com.example.airplane_entertainment_system.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.airplane_entertainment_system.presentation.components.AirportRow
import com.example.airplane_entertainment_system.presentation.components.FlightCard
import com.example.airplane_entertainment_system.presentation.components.FlightRouteInfo
import com.example.airplane_entertainment_system.viewmodels.TestFlightData

@Preview
@Composable
fun FlightCardPreview() {
    FlightCard(
        flight = TestFlightData.testFlight,
        onClick = {}
    )
}

@Preview
@Composable
fun FlightCardLoadingPreview() {
    // In a real preview, you'd show a loading skeleton
    // For now, this demonstrates preview structure
    FlightCard(
        flight = TestFlightData.testFlight,
        onClick = {}
    )
}

@Preview
@Composable
fun RouteInfoPreview() {
    FlightRouteInfo(
        departure = TestFlightData.testFlight.departure,
        arrival = TestFlightData.testFlight.arrival
    )
}

@Preview
@Composable
fun AirportRowPreview() {
    AirportRow(
        code = "LAX",
        city = "Los Angeles",
        time = "10:00 AM",
        terminal = "Terminal 2"
    )
}

@Preview
@Composable
fun FlightListScreenPreview() {
    FlightListScreen(onFlightSelected = {})
}
