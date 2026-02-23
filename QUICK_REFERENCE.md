# Quick Reference: Build & Run

## Project Status
✅ **Android Target**: Compiles successfully
⚠️ **iOS Target**: KMP framework ready, requires Xcode setup

## Build Commands

### Compile Android Target
```bash
cd /Users/stefan.hodges-kluck/IdeaProjects/airplane_entertainment_system
./gradlew :composeApp:compileDebugKotlinAndroid
```

### Build Android APK
```bash
./gradlew :composeApp:assembleDebug
```

### Run Tests
```bash
./gradlew :composeApp:testDebugUnitTest
```

### Build iOS Framework
```bash
./gradlew :composeApp:iosSimulatorArm64MainKlibrary
```

## Project Structure Quick Navigation

```
airplane_entertainment_system/
├── composeApp/
│   └── src/
│       ├── commonMain/
│       │   └── kotlin/com/example/airplane_entertainment_system/
│       │       ├── domain/models/           ← Data models (Flight, AirportInfo)
│       │       ├── data/
│       │       │   ├── mock/                ← MockFlightData (demo data)
│       │       │   ├── remote/              ← API client (FlightApiService)
│       │       │   └── repositories/        ← Data abstraction layer
│       │       ├── presentation/
│       │       │   ├── state/               ← UI state (sealed classes)
│       │       │   ├── viewmodels/          ← FlightList/DetailViewModel
│       │       │   ├── screens/             ← UI screens
│       │       │   └── components/          ← Reusable composables
│       │       ├── navigation/              ← Routes & NavHost
│       │       └── App.kt                   ← Entry point
│       ├── commonTest/
│       │   └── kotlin/com/example/...
│       │       ├── viewmodels/              ← ViewModel tests
│       │       ├── repositories/            ← Repository tests
│       │       ├── mocks/                   ← Test doubles
│       │       └── presentation/            ← Composable previews
│       ├── androidMain/                     ← Android-specific code
│       └── iosMain/                         ← iOS-specific code
├── iosApp/                                  ← iOS SwiftUI wrapper
├── gradle/libs.versions.toml                ← Dependency versions
├── composeApp/build.gradle.kts              ← Build configuration
└── IMPLEMENTATION_SUMMARY.md                ← Full documentation
```

## Key Files & Their Purpose

### Data Layer
- **Flight.kt** - Data classes with `@Serializable` annotation
- **FlightApiService.kt** - Interface defining API operations
- **HttpClientFactory.kt** - Ktor client configuration
- **MockFlightApiClient.kt** - Returns mock data
- **FlightRepository.kt** - Wraps API client, returns `Flow<Result<T>>`

### Presentation Layer
- **UiState.kt** - Sealed classes for `FlightListUiState` and `FlightDetailUiState`
- **FlightListViewModel.kt** - Manages flight list state with `StateFlow`
- **FlightDetailViewModel.kt** - Manages single flight state
- **FlightListScreen.kt** - Main UI for flight list
- **FlightDetailScreen.kt** - Detail UI for single flight
- **FlightComponents.kt** - Reusable composables (FlightCard, RouteInfo, etc.)

### Navigation
- **NavRoutes.kt** - Type-safe route definitions
- **AppNavHost.kt** - Navigation graph

### Testing
- **FlightListViewModelTest.kt** - ViewModel unit tests using Turbine
- **FlightDetailViewModelTest.kt** - Detail ViewModel tests
- **FlightRepositoryTest.kt** - Repository layer tests
- **MockFlightRepository.kt** - Test double for repositories
- **TestFlightData.kt** - Sample test data
- **ScreenPreviews.kt** - Composable preview tests

## Common Tasks

### Add New Flight Screen

1. Create `NewFlightScreen.kt` in `presentation/screens/`
2. Create `NewFlightViewModel.kt` in `presentation/viewmodels/` with `StateFlow<NewFlightUiState>`
3. Create `NewFlightUiState.kt` in `presentation/state/`
4. Add route to `NavRoutes.kt`
5. Add composable to `AppNavHost.kt`
6. Create tests in `commonTest/kotlin/`

### Swap to Real API

1. Modify `FlightApiService.kt` to add actual HTTP methods (or use Ktor client)
2. Create `RealFlightApiClient.kt` implementing `FlightApiService`
3. In screen, change:
   ```kotlin
   val apiClient: FlightApiService = RealFlightApiClient()  // instead of MockFlightApiClient()
   ```
4. No ViewModel/Repository changes needed!

### Run Specific Test

```bash
./gradlew :composeApp:testDebugUnitTest --tests "*FlightListViewModelTest*"
```

### View Composable Preview in IDE

1. Open any screen file (e.g., `FlightListScreen.kt`)
2. Look for `@Composable` functions with `@Preview`
3. Right-click → "Show Preview" or use preview pane on right

## Architecture Pattern Reminder

```
User Action
    ↓
Screen calls ViewModel method
    ↓
ViewModel updates _uiState.value = newState
    ↓
StateFlow emits new state
    ↓
collectAsState() in Composable observes
    ↓
Composable recomposes
    ↓
Screen updates
```

## Dependencies & Versions

| Library | Version | Purpose |
|---------|---------|---------|
| Kotlin | 2.3.0 | Language |
| Compose Multiplatform | 1.10.0 | UI Framework |
| Coroutines | 1.8.1 | Async/Flow |
| Ktor Client | 2.3.8 | HTTP (cross-platform) |
| Kotlinx Serialization | 1.6.3 | JSON |
| Navigation Compose | 2.8.5 | Routing |
| Turbine | 1.1.0 | Flow testing |
| Mockk | 1.13.13 | Mocking |

## Troubleshooting

### Build fails: "Unresolved reference"
→ Run `./gradlew clean` then rebuild

### Tests not found
→ Ensure test files in `commonTest/kotlin/` follow naming: `*Test.kt`

### Preview not showing
→ Add `@Preview @Composable` annotation to function

### Coroutines compile error
→ Check coroutines version matches in `libs.versions.toml`

### iOS Framework build slow
→ First time takes longer; subsequent builds cached

## Documentation

- **IMPLEMENTATION_SUMMARY.md** - Complete architecture overview
- **ARCHITECTURE_REASONING.md** - Why each decision was made
- **This file** - Quick reference for daily use

## Next Steps

1. **Understand architecture** → Read IMPLEMENTATION_SUMMARY.md
2. **Learn reasoning** → Read ARCHITECTURE_REASONING.md
3. **Build & test** → Run `./gradlew assembleDebug`
4. **Extend features** → Follow "Add New Flight Screen" steps
5. **Deploy** → Use Android Studio's build system or CI/CD

---

**For questions or issues**: Check ARCHITECTURE_REASONING.md for "Why" answers

