package com.example.airplane_entertainment_system

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.airplane_entertainment_system.navigation.AppNavHost

@Composable
actual fun AppContent() {
    val navController = rememberNavController()
    AppNavHost(navController = navController)
}

