package com.example.airplane_entertainment_system.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.airplane_entertainment_system.presentation.screens.FlightDetailScreen
import com.example.airplane_entertainment_system.presentation.screens.FlightListScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = FlightListRoute
    ) {
        composable<FlightListRoute> {
            FlightListScreen(
                onFlightSelected = { flightId ->
                    navController.navigate(FlightDetailRoute(flightId = flightId))
                }
            )
        }

        composable<FlightDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<FlightDetailRoute>()
            FlightDetailScreen(
                flightId = route.flightId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

