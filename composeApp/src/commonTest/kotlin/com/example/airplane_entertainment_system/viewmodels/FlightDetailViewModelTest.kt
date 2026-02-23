package com.example.airplane_entertainment_system.viewmodels

import app.cash.turbine.turbineScope
import com.example.airplane_entertainment_system.presentation.state.FlightDetailUiState
import com.example.airplane_entertainment_system.presentation.viewmodels.FlightDetailViewModel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs

class FlightDetailViewModelTest {

    @Test
    fun testLoadFlightDetailSuccess() = runTest {
        turbineScope {
            val repository = MockFlightRepository(flights = TestFlightData.testFlightList)
            val viewModel = FlightDetailViewModel(repository)

            val uiStateTurbine = viewModel.uiState.testIn(backgroundScope)

            // Initial state should be Loading
            assertIs<FlightDetailUiState.Loading>(uiStateTurbine.awaitItem())

            // Load flight detail
            viewModel.loadFlightDetail("1")

            // Should emit Loading again
            assertIs<FlightDetailUiState.Loading>(uiStateTurbine.awaitItem())

            // Next state should be Success with flight
            val successState = uiStateTurbine.awaitItem()
            assertIs<FlightDetailUiState.Success>(successState)
            assert(successState.flight.flightNumber == "TEST101")
            assert(successState.flight.id == "1")

            uiStateTurbine.cancel()
        }
    }

    @Test
    fun testLoadFlightDetailNotFound() = runTest {
        turbineScope {
            val repository = MockFlightRepository(flights = TestFlightData.testFlightList)
            val viewModel = FlightDetailViewModel(repository)

            val uiStateTurbine = viewModel.uiState.testIn(backgroundScope)

            // Skip initial Loading
            uiStateTurbine.awaitItem()

            // Load non-existent flight
            viewModel.loadFlightDetail("999")

            // Should emit Loading
            assertIs<FlightDetailUiState.Loading>(uiStateTurbine.awaitItem())

            // Next state should be Error
            val errorState = uiStateTurbine.awaitItem()
            assertIs<FlightDetailUiState.Error>(errorState)
            assert(errorState.message.contains("not found", ignoreCase = true))

            uiStateTurbine.cancel()
        }
    }

    @Test
    fun testLoadFlightDetailError() = runTest {
        turbineScope {
            val repository = MockFlightRepository(shouldFail = true)
            val viewModel = FlightDetailViewModel(repository)

            val uiStateTurbine = viewModel.uiState.testIn(backgroundScope)

            // Skip initial Loading
            uiStateTurbine.awaitItem()

            // Load flight
            viewModel.loadFlightDetail("1")

            // Should emit Loading
            assertIs<FlightDetailUiState.Loading>(uiStateTurbine.awaitItem())

            // Next state should be Error
            val errorState = uiStateTurbine.awaitItem()
            assertIs<FlightDetailUiState.Error>(errorState)

            uiStateTurbine.cancel()
        }
    }
}
