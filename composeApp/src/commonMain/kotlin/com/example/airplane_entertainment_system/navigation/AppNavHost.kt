package com.example.airplane_entertainment_system.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.airplane_entertainment_system.presentation.screens.FlightDetailScreen
import com.example.airplane_entertainment_system.presentation.screens.FlightListScreen
import kotlin.reflect.typeOf

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.FlightList.route
    ) {
        composable(NavRoutes.FlightList.route) {
            FlightListScreen(
                onFlightSelected = { flightId ->
                    navController.navigate("${NavRoutes.FlightDetail.route}/$flightId")
                }
            )
        }

        composable(
            route = "${NavRoutes.FlightDetail.route}/{${NavRoutes.FlightDetail.flightIdArg}}"
        ) { backStackEntry ->
            val flightId = backStackEntry.arguments?.getString(NavRoutes.FlightDetail.flightIdArg) ?: ""
            FlightDetailScreen(
                flightId = flightId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
