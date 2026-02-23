package com.example.airplane_entertainment_system.data.mock

import com.example.airplane_entertainment_system.domain.models.AirportInfo
import com.example.airplane_entertainment_system.domain.models.BoardingStatus
import com.example.airplane_entertainment_system.domain.models.Flight
import com.example.airplane_entertainment_system.domain.models.SeatClass

object MockFlightData {
    private val flightsList = listOf(
        Flight(
            id = "1",
            flightNumber = "AA101",
            departure = AirportInfo(
                code = "LAX",
                city = "Los Angeles",
                country = "USA",
                terminal = "Terminal 2",
                scheduledTime = "10:30 AM",
                estimatedTime = "10:30 AM"
            ),
            arrival = AirportInfo(
                code = "JFK",
                city = "New York",
                country = "USA",
                terminal = "Terminal 4",
                scheduledTime = "6:45 PM",
                estimatedTime = "6:30 PM"
            ),
            aircraft = "Boeing 777-200",
            gate = "B23",
            boardingStatus = BoardingStatus.IN_PROGRESS,
            seat = "12A",
            seatClass = SeatClass.BUSINESS
        ),
        Flight(
            id = "2",
            flightNumber = "UA205",
            departure = AirportInfo(
                code = "SFO",
                city = "San Francisco",
                country = "USA",
                terminal = "Terminal 3",
                scheduledTime = "2:15 PM",
                estimatedTime = "2:15 PM"
            ),
            arrival = AirportInfo(
                code = "ORD",
                city = "Chicago",
                country = "USA",
                terminal = "Terminal 1",
                scheduledTime = "8:00 PM",
                estimatedTime = "8:15 PM"
            ),
            aircraft = "Airbus A350",
            gate = "C15",
            boardingStatus = BoardingStatus.NOT_STARTED,
            seat = "28F",
            seatClass = SeatClass.ECONOMY
        ),
        Flight(
            id = "3",
            flightNumber = "DL301",
            departure = AirportInfo(
                code = "ATL",
                city = "Atlanta",
                country = "USA",
                terminal = "Terminal S",
                scheduledTime = "11:00 AM",
                estimatedTime = "10:50 AM"
            ),
            arrival = AirportInfo(
                code = "MIA",
                city = "Miami",
                country = "USA",
                terminal = "Terminal E",
                scheduledTime = "1:30 PM",
                estimatedTime = "1:20 PM"
            ),
            aircraft = "Boeing 737-900",
            gate = "A8",
            boardingStatus = BoardingStatus.BOARDING_COMPLETE,
            seat = "15C",
            seatClass = SeatClass.ECONOMY
        )
    )

    fun getFlights(): List<Flight> = flightsList

    fun getFlightById(id: String): Flight? = flightsList.find { it.id == id }
}

