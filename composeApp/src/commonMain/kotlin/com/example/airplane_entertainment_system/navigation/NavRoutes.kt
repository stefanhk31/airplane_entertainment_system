package com.example.airplane_entertainment_system.navigation

sealed class NavRoutes {
    data object FlightList : NavRoutes() {
        const val route = "flight_list"
    }

    data class FlightDetail(val flightId: String) : NavRoutes() {
        companion object {
            const val route = "flight_detail"
            const val flightIdArg = "flight_id"
        }

        fun createRoute(flightId: String) = "$route/$flightId"
    }
}
