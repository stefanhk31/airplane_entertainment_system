package com.example.airplane_entertainment_system.repositories

import app.cash.turbine.turbineScope
import com.example.airplane_entertainment_system.data.repositories.FlightRepositoryImpl
import com.example.airplane_entertainment_system.viewmodels.MockFlightRepository
import com.example.airplane_entertainment_system.viewmodels.TestFlightData
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FlightRepositoryTest {

    @Test
    fun testRepositoryWrapsApiSuccess() = runTest {
        turbineScope {
            val mockApi = MockFlightRepository(flights = TestFlightData.testFlightList)

            val getFlightsTurbine = mockApi.getFlights().testIn(backgroundScope)

            // Should emit success result
            val result = getFlightsTurbine.awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(result.getOrNull()?.size,2)

            getFlightsTurbine.cancel()
        }
    }

    @Test
    fun testRepositoryWrapsApiError() = runTest {
        turbineScope {
            val mockApi = MockFlightRepository(shouldFail = true)

            val getFlightsTurbine = mockApi.getFlights().testIn(backgroundScope)

            // Should emit failure result
            val result = getFlightsTurbine.awaitItem()
            assertTrue(result.isFailure)

            getFlightsTurbine.cancel()
        }
    }

    @Test
    fun testRepositoryReturnsFlightById() = runTest {
        turbineScope {
            val mockApi = MockFlightRepository(flights = TestFlightData.testFlightList)

            val getFlightTurbine = mockApi.getFlightDetail("1").testIn(backgroundScope)

            // Should emit success result with correct flight
            val result = getFlightTurbine.awaitItem()
            assertTrue(result.isSuccess)
            val flight = result.getOrNull()
            assertEquals(flight?.id, "1")
            assertEquals(flight?.flightNumber, "TEST101")

            getFlightTurbine.cancel()
        }
    }
}
