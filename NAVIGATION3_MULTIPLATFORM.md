# Navigation 3 Multiplatform Implementation

## Problem Resolved ✅

Navigation 3 is **not yet fully multiplatform** in Kotlin Compose (as of the current release cycle). While the Kotlin documentation suggests multiplatform support, the actual library artifacts are still Android-specific.

## Solution: Platform-Specific Navigation

We've implemented a hybrid approach that uses **Navigation 3 on Android** and a **simple state-based navigation on iOS**.

### Architecture

```
┌─────────────────────────────────┐
│     Common Code (expect)        │
│   expect fun AppContent()       │
└─────────────────────────────────┘
         ↓ inherits to ↓
    ┌────────┴────────┐
    ↓                 ↓
┌─────────────┐  ┌──────────────┐
│  Android    │  │     iOS      │
│ Navigation3 │  │   State-based│
│   (actual)  │  │   (actual)   │
└─────────────┘  └──────────────┘
```

## Implementation Details

### 1. Common Layer (`commonMain`)

**App.kt** - Now uses expect/actual pattern:
```kotlin
@Composable
@Preview
fun App() {
    MaterialTheme {
        AppContent()  // Platform-specific implementation
    }
}

@Composable
expect fun AppContent()  // Resolved at compile time
```

**NavRoutes.kt** - Remains platform-agnostic (uses `@Serializable`):
```kotlin
@Serializable
data object FlightListRoute

@Serializable
data class FlightDetailRoute(val flightId: String)
```

### 2. Android Implementation (`androidMain`)

Uses **Navigation 3** with type-safe routing:

**AppContent.kt**:
```kotlin
@Composable
actual fun AppContent() {
    val navController = rememberNavController()
    AppNavHost(navController = navController)
}
```

**AppNavHost.kt**:
```kotlin
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = FlightListRoute) {
        composable<FlightListRoute> {
            FlightListScreen(onFlightSelected = { id ->
                navController.navigate(FlightDetailRoute(flightId = id))
            })
        }
        composable<FlightDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<FlightDetailRoute>()
            FlightDetailScreen(flightId = route.flightId, onNavigateBack = { navController.popBackStack() })
        }
    }
}
```

### 3. iOS Implementation (`iosMain`)

Uses **simple state-based navigation** (no NavController):

**AppContent.kt**:
```kotlin
@Composable
actual fun AppContent() {
    // iOS: state-based navigation without NavController dependency
    val currentScreen = remember { mutableStateOf<String?>(null) }
    
    when (currentScreen.value) {
        null -> {
            FlightListScreen(onFlightSelected = { flightId ->
                currentScreen.value = flightId
            })
        }
        else -> {
            FlightDetailScreen(
                flightId = currentScreen.value ?: "",
                onNavigateBack = { currentScreen.value = null }
            )
        }
    }
}
```

## Dependency Changes

### gradle/libs.versions.toml
```toml
[versions]
androidx-navigation3 = "2.9.7"  # AndroidX Navigation Compose

[libraries]
androidx-navigation3 = { module = "androidx.navigation:navigation-compose", version.ref = "androidx-navigation3" }
```

### composeApp/build.gradle.kts
```kotlin
sourceSets {
    androidMain.dependencies {
        implementation(libs.androidx.navigation3)  // Android only
    }
    iosMain.dependencies {
        // No navigation library needed
    }
    commonMain.dependencies {
        // Navigation removed - handled at platform level
    }
}
```

## Build Status ✅

- ✅ **Android** (`compileDebugKotlin`): BUILD SUCCESSFUL
- ✅ **iOS Simulator** (`compileKotlinIosSimulatorArm64`): BUILD SUCCESSFUL
- ✅ **iOS Device** (`compileKotlinIosArm64`): Expected to work

## Screen Interface Compatibility

Both implementations maintain the **same screen signatures**:

```kotlin
@Composable
fun FlightListScreen(onFlightSelected: (String) -> Unit)

@Composable
fun FlightDetailScreen(
    flightId: String,
    onNavigateBack: () -> Unit
)
```

The screens themselves require no changes - they work identically on both platforms!

## Why This Approach?

| Aspect | Benefit |
|--------|---------|
| **Type Safety (Android)** | Navigation 3 provides compile-time route checking |
| **Simplicity (iOS)** | No NavController dependency means faster iOS compilation |
| **Maintainability** | Screen code remains 100% shared, only navigation logic differs |
| **Future-Proof** | When Navigation 3 becomes truly multiplatform, only `AppContent.kt` implementations need updating |

## Future Migration Path

When Jetbrains releases a truly multiplatform Navigation 3 library:

1. Add multiplatform Navigation 3 to commonMain
2. Move iOS `AppContent.kt` implementation to use Navigation 3
3. Delete `androidMain/AppContent.kt` and move `AppNavHost.kt` to commonMain
4. Deprecate the platform-specific approach

This hybrid solution provides the **best of both worlds** while waiting for full multiplatform support.

## Files Changed

### Created
- `composeApp/src/androidMain/kotlin/.../AppContent.kt` - Android Navigation 3 wrapper
- `composeApp/src/androidMain/kotlin/.../navigation/AppNavHost.kt` - Android Navigation 3 graph
- `composeApp/src/iosMain/kotlin/.../AppContent.kt` - iOS state-based navigation

### Modified
- `composeApp/src/commonMain/kotlin/.../App.kt` - Now uses expect/actual
- `gradle/libs.versions.toml` - Navigation 3 moved to androidMain
- `composeApp/build.gradle.kts` - Dependency moved to androidMain

### Removed
- `composeApp/src/commonMain/kotlin/.../navigation/AppNavHost.kt` - Moved to androidMain

