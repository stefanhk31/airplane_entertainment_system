package com.example.airplane_entertainment_system.viewmodels

import com.example.airplane_entertainment_system.domain.models.AirportInfo
import com.example.airplane_entertainment_system.domain.models.BoardingStatus
import com.example.airplane_entertainment_system.domain.models.Flight
import com.example.airplane_entertainment_system.domain.models.SeatClass

object TestFlightData {
    val testFlight = Flight(
        id = "1",
        flightNumber = "TEST101",
        departure = AirportInfo(
            code = "LAX",
            city = "Los Angeles",
            country = "USA",
            terminal = "1",
            scheduledTime = "10:00 AM",
            estimatedTime = "10:00 AM"
        ),
        arrival = AirportInfo(
            code = "JFK",
            city = "New York",
            country = "USA",
            terminal = "4",
            scheduledTime = "6:00 PM",
            estimatedTime = "6:00 PM"
        ),
        aircraft = "Boeing 777",
        gate = "A1",
        boardingStatus = BoardingStatus.NOT_STARTED,
        seat = "1A",
        seatClass = SeatClass.BUSINESS
    )

    val testFlightList = listOf(
        testFlight,
        testFlight.copy(id = "2", flightNumber = "TEST202")
    )
}
