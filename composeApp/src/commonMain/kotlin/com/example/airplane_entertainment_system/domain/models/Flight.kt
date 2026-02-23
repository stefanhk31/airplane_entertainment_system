package com.example.airplane_entertainment_system.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Flight(
    val id: String,
    val flightNumber: String,
    val departure: AirportInfo,
    val arrival: AirportInfo,
    val aircraft: String,
    val gate: String?,
    val boardingStatus: BoardingStatus,
    val seat: String?,
    val seatClass: SeatClass = SeatClass.ECONOMY
)

@Serializable
data class AirportInfo(
    val code: String,
    val city: String,
    val country: String,
    val terminal: String?,
    val scheduledTime: String,
    val estimatedTime: String?
)

@Serializable
enum class BoardingStatus {
    NOT_STARTED,
    IN_PROGRESS,
    BOARDING_COMPLETE,
    CLOSED,
    CANCELLED
}

@Serializable
enum class SeatClass {
    ECONOMY,
    BUSINESS,
    FIRST
}

