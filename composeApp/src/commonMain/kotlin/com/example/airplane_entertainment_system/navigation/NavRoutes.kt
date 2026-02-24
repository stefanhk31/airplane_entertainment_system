package com.example.airplane_entertainment_system.navigation

import kotlinx.serialization.Serializable

@Serializable
data object FlightListRoute

@Serializable
data class FlightDetailRoute(val flightId: String)
