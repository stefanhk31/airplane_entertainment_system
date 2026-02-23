package com.example.airplane_entertainment_system

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform