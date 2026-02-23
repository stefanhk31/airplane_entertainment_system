# Architecture Diagrams & Visual Guides

## 1. High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     USER INTERACTION                         │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
        ┌────────────────────────────────────┐
        │      COMPOSABLE UI LAYER           │
        │  @Composable fun FlightListScreen  │
        │  - Observes StateFlow via          │
        │    collectAsState()                │
        │  - Renders based on UiState        │
        │  - Calls ViewModel methods         │
        └────────────────┬───────────────────┘
                         │
                         ▼
        ┌────────────────────────────────────┐
        │      VIEWMODEL LAYER               │
        │  class FlightListViewModel :       │
        │    ViewModel                       │
        │  - Owns StateFlow<UiState>         │
        │  - Transforms data to UI state     │
        │  - Handles user actions            │
        │  - Lifecycle-aware                 │
        └────────────────┬───────────────────┘
                         │
                         ▼
        ┌────────────────────────────────────┐
        │      REPOSITORY LAYER              │
        │  interface FlightRepository        │
        │  - Returns Flow<Result<T>>         │
        │  - Abstracts data source           │
        │  - Handles errors gracefully       │
        │  - Testable with mock client       │
        └────────────────┬───────────────────┘
                         │
                         ▼
        ┌────────────────────────────────────┐
        │   DATA CLIENT LAYER                │
        │  interface FlightApiService        │
        │  implementations:                  │
        │  - MockFlightApiClient (demo)      │
        │  - RealFlightApiClient (future)    │
        │  - Uses Ktor HTTP (KMP)            │
        └────────────────────────────────────┘
```

## 2. State Flow for Flight List

```
     ┌─────────────────────────┐
     │  Initial State          │
     │  Loading                │
     └────────────┬────────────┘
                  │
        viewModel.loadFlights()
                  │
                  ▼
     ┌─────────────────────────┐
     │  Repository.getFlights()│
     │  emits Flow             │
     └────────────┬────────────┘
                  │
                  ▼
     ┌─────────────────────────┐
     │  API Client returns     │
     │  List<Flight>           │
     └────────────┬────────────┘
                  │
                  ▼
     ┌─────────────────────────────────┐
     │  ViewModel receives result      │
     │  Result.fold() on success:      │
     │  _uiState.value =               │
     │    Success(flights)             │
     └────────────┬────────────────────┘
                  │
                  ▼
     ┌──────────────────────────────────┐
     │  StateFlow emits new state       │
     │  collectAsState() observes       │
     │  Composable recomposes           │
     │  UI updates with flight list     │
     └──────────────────────────────────┘
```

## 3. Error Handling Flow

```
     ┌──────────────────────────┐
     │  API Call Throws Error   │
     └────────────┬─────────────┘
                  │
                  ▼
     ┌──────────────────────────────────┐
     │  Repository.catch()              │
     │  emits Result.failure()           │
     └────────────┬─────────────────────┘
                  │
                  ▼
     ┌──────────────────────────────────┐
     │  ViewModel receives Result       │
     │  Result.fold() on failure:       │
     │  _uiState.value =                │
     │    Error(error.message)          │
     └────────────┬─────────────────────┘
                  │
                  ▼
     ┌──────────────────────────────────┐
     │  StateFlow emits error state     │
     │  UI shows error message          │
     │  User can retry                  │
     └──────────────────────────────────┘
```

## 4. Sealed Class State Diagram

```
              FlightListUiState
             /        |         \
            /         |          \
    ┌─────────┐   ┌───────┐   ┌────────────┐
    │ Loading │   │Success│   │  Error     │
    │         │   │       │   │            │
    │ (no    │   │flights│   │ message:   │
    │  data) │   │: List │   │ String     │
    └─────────┘   │       │   └────────────┘
                  └───────┘
                  
Compose UI Pattern:
when (uiState) {
    is Loading → ShowLoadingSpinner()
    is Success → ShowFlightList(flights)
    is Error → ShowErrorMessage(message)
}
```

## 5. Navigation Flow

```
        ┌──────────────────────┐
        │  App() Entry Point   │
        │  rememberNavController
        │  AppNavHost          │
        └──────┬───────────────┘
               │
        ┌──────▼────────────────────────────┐
        │  NavHost(startDestination:        │
        │    FlightList.route)              │
        └──────┬─────────────────────────────┘
               │
        ┌──────┴──────┐
        │             │
        ▼             ▼
    ┌──────────────┐  ┌──────────────────┐
    │FlightList    │  │FlightDetail      │
    │Composable    │  │Composable        │
    │              │  │                  │
    │onClick:      │  │onNavigateBack:   │
    │navigate(     │  │popBackStack()    │
    │  "flight_    │  │                  │
    │  detail/$id" │  │                  │
    └──────────────┘  └──────────────────┘
```

## 6. Test Pyramid Architecture

```
                 ┌────────────────┐
                 │  UI Previews   │
                 │  @Preview      │
                 │  5 tests       │
                 └────────────────┘
                 /                  \
            ┌──────────────────────────────┐
            │  ViewModel Tests (Turbine)   │
            │  - State transitions         │
            │  - Error handling            │
            │  6 tests                     │
            └──────────────────────────────┘
           /                                 \
        ┌──────────────────────────────────────┐
        │  Repository Tests (Flow)            │
        │  - Data wrapping                    │
        │  - Result handling                  │
        │  3 tests                            │
        └──────────────────────────────────────┘
       /                                      \
    ┌─────────────────────────────────────────┐
    │  Data Client (Mocked)                  │
    │  - No tests needed (mock implementation)|
    └─────────────────────────────────────────┘
```

## 7. File Dependency Graph

```
                    App.kt
                      │
                      ▼
            ┌─────────────────────┐
            │   NavRoutes.kt      │
            │   AppNavHost.kt     │
            └──────┬──────────────┘
                   │
        ┌──────────┴──────────────┐
        ▼                         ▼
  ┌──────────────┐         ┌──────────────┐
  │FlightList    │         │FlightDetail  │
  │Screen.kt     │         │Screen.kt     │
  └──────┬───────┘         └──────┬───────┘
         │                        │
         └────────────┬───────────┘
                      ▼
        ┌──────────────────────────┐
        │  FlightListViewModel     │
        │  FlightDetailViewModel   │
        └──────────────┬───────────┘
                       │
                       ▼
        ┌──────────────────────────┐
        │  UiState.kt              │
        └──────────────┬───────────┘
                       │
        ┌──────────────┴──────────────┐
        ▼                             ▼
  ┌──────────────┐           ┌──────────────┐
  │Repository    │           │FlightComponents
  │Interface     │           │.kt
  └──────┬───────┘           └──────────────┘
         │
         ▼
  ┌──────────────────────────┐
  │FlightRepositoryImpl       │
  └──────────┬───────────────┘
             │
  ┌──────────┴──────────────┐
  ▼                         ▼
  ┌──────────────┐    ┌─────────────────┐
  │FlightApiSvc  │    │Flight.kt Models │
  └──────┬───────┘    └─────────────────┘
         │
         ▼
  ┌──────────────────┐
  │HttpClientFactory │
  │MockFlightData    │
  └──────────────────┘
```

## 8. Data Flow: User Clicks Flight

```
1. User clicks FlightCard
   │
   ▼
2. onClick handler calls:
   onFlightSelected(flight.id)
   │
   ▼
3. Screen's lambda calls:
   navController.navigate("flight_detail/$id")
   │
   ▼
4. Navigation routes to FlightDetailScreen
   │
   ▼
5. FlightDetailScreen created with:
   flightId = "1"
   viewModel = FlightDetailViewModel(repository)
   │
   ▼
6. remember { viewModel.loadFlightDetail(id) }
   │
   ▼
7. ViewModel calls:
   repository.getFlightDetail(id)
   │
   ▼
8. Repository calls:
   apiClient.getFlightDetail(id)
   │
   ▼
9. Mock client returns:
   Flight(id="1", flightNumber="AA101", ...)
   │
   ▼
10. Repository wraps in:
    Result.success(flight)
    │
    ▼
11. ViewModel receives Result
    _uiState.value = Success(flight)
    │
    ▼
12. StateFlow emits Success
    │
    ▼
13. collectAsState() observes
    │
    ▼
14. Composable recomposes
    │
    ▼
15. FlightDetailScreen renders
    flight information
```

## 9. Testing Architecture

```
┌─────────────────────────────────┐
│  ViewModel Test                 │
├─────────────────────────────────┤
│  1. Create MockRepository       │
│  2. Create ViewModel(repo)      │
│  3. Use Turbine to observe      │
│     uiState emissions           │
│  4. Assert state changes        │
│                                 │
│  viewModel.uiState.testIn()     │
│    .awaitItem() // Loading      │
│    .awaitItem() // Success      │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│  Repository Test                │
├─────────────────────────────────┤
│  1. Create MockApiClient        │
│  2. Create Repository(client)   │
│  3. Collect Flow emissions      │
│  4. Assert Result<T>            │
│                                 │
│  repository.getFlights()        │
│    .collect { result ->         │
│      assertTrue(result.isSuccess)
│    }                            │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│  Composable Preview             │
├─────────────────────────────────┤
│  @Preview                       │
│  @Composable                    │
│  fun FlightCardPreview() {      │
│    FlightCard(                  │
│      flight = testFlight,       │
│      onClick = {}               │
│    )                            │
│  }                              │
│                                 │
│  - IDE Live Preview             │
│  - Snapshot testing             │
│  - State-driven previews        │
└─────────────────────────────────┘
```

## 10. State Management Comparison

```
FLUTTER (Provider Pattern)
┌────────────────────────────────┐
│ class FlightProvider            │
│   extends ChangeNotifier        │
│ {                               │
│   List<Flight> flights = [];    │
│   bool isLoading = false;       │
│   String? error;                │
│                                 │
│   Future<void> load() async {   │
│     isLoading = true;           │
│     notifyListeners();          │
│     flights = await api.get();  │
│     notifyListeners();          │
│   }                             │
│ }                               │
└────────────────────────────────┘

KOTLIN/COMPOSE (MVVM + StateFlow)
┌────────────────────────────────┐
│ sealed class FlightListUiState  │
│   object Loading                │
│   data class Success(flights)   │
│   data class Error(message)     │
│                                 │
│ class FlightListViewModel(      │
│   repo: FlightRepository        │
│ ) : ViewModel() {               │
│   private val _uiState =        │
│     MutableStateFlow<...>(...)  │
│   val uiState = _uiState        │
│     .asStateFlow()              │
│                                 │
│   fun loadFlights() {           │
│     viewModelScope.launch {     │
│       _uiState.value =          │
│         Success(flights)        │
│     }                           │
│   }                             │
│ }                               │
└────────────────────────────────┘
```

---

## Key Takeaways From Diagrams

1. **Unidirectional data flow**: User → UI → ViewModel → Repository → Client → back to UI
2. **Sealed classes for states**: Type-safe, compiler enforces handling all cases
3. **Testing at each layer**: Mock lower layers, test upper layers independently
4. **StateFlow reactivity**: Automatic notification, no manual observer pattern
5. **Navigation type-safety**: Routes defined as sealed classes, compiler checks
6. **Error handling as values**: Result<T> wraps success and failure naturally

