package com.example.airplane_entertainment_system.viewmodels

import app.cash.turbine.turbineScope
import com.example.airplane_entertainment_system.presentation.state.FlightListUiState
import com.example.airplane_entertainment_system.presentation.viewmodels.FlightListViewModel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs

class FlightListViewModelTest {

    @Test
    fun testLoadFlightsSuccess() = runTest {
        turbineScope {
            val repository = MockFlightRepository(flights = TestFlightData.testFlightList)
            val viewModel = FlightListViewModel(repository)

            val uiStateTurbine = viewModel.uiState.testIn(backgroundScope)

            // Initial state should be Loading
            assertIs<FlightListUiState.Loading>(uiStateTurbine.awaitItem())

            // Next state should be Success with flights
            val successState = uiStateTurbine.awaitItem()
            assertIs<FlightListUiState.Success>(successState)
            assert(successState.flights.size == 2)
            assert(successState.flights[0].flightNumber == "TEST101")

            uiStateTurbine.cancel()
        }
    }

    @Test
    fun testLoadFlightsError() = runTest {
        turbineScope {
            val repository = MockFlightRepository(shouldFail = true)
            val viewModel = FlightListViewModel(repository)

            val uiStateTurbine = viewModel.uiState.testIn(backgroundScope)

            // Initial state should be Loading
            assertIs<FlightListUiState.Loading>(uiStateTurbine.awaitItem())

            // Next state should be Error
            val errorState = uiStateTurbine.awaitItem()
            assertIs<FlightListUiState.Error>(errorState)
            assert(errorState.message.isNotEmpty())

            uiStateTurbine.cancel()
        }
    }

    @Test
    fun testRefreshFlights() = runTest {
        turbineScope {
            val repository = MockFlightRepository(flights = TestFlightData.testFlightList)
            val viewModel = FlightListViewModel(repository)

            val uiStateTurbine = viewModel.uiState.testIn(backgroundScope)

            // Skip initial Loading and Success
            uiStateTurbine.awaitItem()
            uiStateTurbine.awaitItem()

            // Refresh
            viewModel.refresh()

            // Should emit Loading again
            assertIs<FlightListUiState.Loading>(uiStateTurbine.awaitItem())

            // Then Success
            val successState = uiStateTurbine.awaitItem()
            assertIs<FlightListUiState.Success>(successState)

            uiStateTurbine.cancel()
        }
    }
}
