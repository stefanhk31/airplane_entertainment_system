package com.example.airplane_entertainment_system

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        AppContent()
    }
}

@Composable
expect fun AppContent()

