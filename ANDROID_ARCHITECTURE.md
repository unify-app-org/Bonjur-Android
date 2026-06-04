# Bonjur Android — Architecture Reference

> Generated: 2026-06-04 | Build system: Gradle + Version Catalogs | Language: Kotlin + Compose

---

## Module Hierarchy

```
Bonjur-Android/
├── app/                     — entry point, navigation host, tab bar
├── appCore/
│   ├── appFoundation/       — MVI base classes (FeatureViewModel, FeatureStore, etc.)
│   ├── appUtils/            — environment config, extensions
│   ├── designSystem/        — Compose theme + 20+ reusable components
│   ├── navigation/          — type-safe routing (Navigator, AppScreens, NavArgs)
│   ├── network/             — Ktor HTTP client, ApiClient, TokenManager
│   └── storage/             — SharedPreferences (plain + AES-256 encrypted)
└── appFeatures/
    ├── auth/
    ├── clubs/
    ├── communities/
    ├── discover/
    ├── groups/
    ├── hangouts/
    ├── profile/
    └── events/              (referenced but folder may be in clubs/groups)
```

**Total source files**: ~289 Kotlin files (7 app + 68 appCore + 214 appFeatures)

---

## Architecture Pattern: MVI

Ported from iOS's UIFeature/FeatureStore pattern. Each screen owns a `State`, handles `Action`s, and emits `SideEffect`s.

```
User tap → Action → ViewModel.handle() → updateState() → Recompose
                                       → postEffect()  → Navigation / Alert / Toast
```

### Base classes (`appCore/appFoundation`)

| Class | Role |
|---|---|
| `FeatureState` | Marker interface — immutable UI state |
| `FeatureAction` | Sealed marker interface — user intents |
| `SideEffect` | Marker interface — one-time events |
| `FeatureStore<State, Action, Effect>` | State holder via `mutableStateOf`; exposes `send(action)` |
| `FeatureViewModel<State, Action, Effect>` | Abstract ViewModel — holds store, implements `handle(action)`, `postEffect()` |
| `FeatureScreen` | Composable wrapper — binds ViewModel to UI with effect handling |

---

## Dependency Injection: Hilt

```kotlin
@HiltAndroidApp class MainApplication : Application()
@AndroidEntryPoint class MainActivity : ComponentActivity()
```

Each feature data layer has a `DataModule`:

```kotlin
@Module @InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds @Singleton
    abstract fun bindClubsDataSource(impl: ClubsDataSourceImpl): ClubsDataSource
}
```

ViewModels use `@HiltViewModel` + `@Inject constructor`.  
Dependencies injected: `ApiClient`, `TokenManager`, `DefaultStorage`, `SecureStorage`, UseCase impls.

---

## Navigation

**Type-safe routing via Compose Navigation + sealed classes**

### Core types (`appCore/navigation`)

| Type | Role |
|---|---|
| `AppScreens` | Top-level sealed: `Auth` \| `Main` |
| `MainScreen` | Feature routes: `TabBar`, `Discover`, `Clubs`, `Events`, `Hangouts`, `Groups` |
| `Navigator` | Singleton — sends `NavigationCommand` via `Channel<NavigationCommand>` |
| `NavigationCommand` | Sealed: `NavigateTo`, `NavigateAndClearStack`, `NavigateUp`, `PopToRoute` |
| `NavArgs` | In-memory object store for passing complex input data between screens |
| `NavigationEffect` | Composable consuming `navigator.navigationFlow` and calling NavHost APIs |

### Input data pattern

```kotlin
// Sender (ViewModel):
NavArgs.put(ClubDetailsInputData(clubId = id))
navigator.navigateTo(ClubsScreens.Details)

// Receiver (Screen):
val input = NavArgs.get<ClubDetailsInputData>()
```

> **Note**: `NavArgs` is runtime-only (not serialized). Losing NavArgs on process death is a known risk.

### Navigation graph

```
AppNavigation (NavHost)
  Auth flow (not logged in)
    Auth.Onboarding
      → Auth.ChooseUniversity
        → Auth.SignIn
          → Auth.Welcome
            → Auth.OptionalInfo (if isFirstLogin)
              → Main.TabBar

  Main flow (logged in)
    Main.TabBar (AppTabBar)
      [0] Discover   → Discover screens
      [1] Clubs      → Clubs.List → Clubs.Details → Clubs.Create
      [2] Groups     → Groups screens
      [3] Hangouts   → Hangouts screens
      [4] Profile    → Profile screens (⚠ not yet wired into MainScreen nav)
      
      Communities    ⚠ not yet wired into MainScreen nav
```

Each feature exposes a `NavGraphBuilder` extension:
```kotlin
fun NavGraphBuilder.clubsNavGraph(navigator: Navigator) { /* composable() calls */ }
```

---

## Network Layer: Ktor

### Stack

```
AppEndpoint (sealed class per feature)
  ↓
ApiClient.request<T>() / requestRawData()
  ↓
Ktor HttpClient (Android engine)
  JSON: kotlinx.serialization
  Timeout: 30s (request / connect / socket)
  Default header: Content-Type: application/json
```

### `AppEndpoint` interface

```kotlin
interface AppEndpoint {
    val path: String
    val method: HttpMethod          // GET, POST, PUT, PATCH, DELETE
    val headers: Map<String, String>
    val body: Any?
    val queryParameters: Map<String, String>?
    val requiresAuth: Boolean       // default true
}
```

### `ApiClient`

- `suspend inline fun <reified T> request(endpoint): T`
- `suspend fun requestRawData(endpoint): ByteArray`
- Auto-retry on **401**: calls `TokenManager.refreshToken()` then retries once
- If refresh fails: clears tokens → posts `ApiException.Unauthorized`
- `NetworkLogger` logs method, URL, status, duration for every call

### Error hierarchy

```
ApiException
  ├── Unauthorized
  ├── ServerError(code: Int, message: String)
  ├── DecodingError
  └── NetworkException
```

### `NetworkService` (DataSource base class)

```kotlin
abstract class NetworkService(@Inject val apiClient: ApiClient) {
    suspend inline fun <reified T> fetch(endpoint: AppEndpoint): T
    suspend fun fetchRawData(endpoint: AppEndpoint): ByteArray
}
```

---

## Storage Layer

| Interface | Impl | Backing store | Use |
|---|---|---|---|
| `DefaultStorage` | `DefaultStorageImpl` | Plain SharedPreferences | `IS_AUTHENTICATED`, communityId, user prefs |
| `SecureStorage` | `SecureStorageImpl` | `EncryptedSharedPreferences` (AES-256-GCM) | access token, refresh token |

Keys defined in enums: `DefaultStorageKey`, `SecureStorageKey`.

---

## Design System (`appCore/designSystem`)

**Theme**: `BonjurTheme` wraps `MaterialTheme` (Material3)  
Light + Dark color schemes; dynamic color on Android 12+  
Typography: `AppMaterialTypography`

**Component library**:

| Component | Purpose |
|---|---|
| `LoadingComponent` | Full-screen / inline loading |
| `CachedAsyncImage` | Coil-backed image with local cache |
| `SearchView` | Search input |
| `AppAlertView` / `AppAlertPresenter` | Global overlay alert (singleton) |
| `CardBackgroundView` | Card styling |
| `CategoriesChipView` | Multi-category chips |
| `SegmentedPickerView` | Segment control |
| `TabView` | Custom tab strip |
| `FilterView` | Filter bottom sheet |
| `SelectableListView` | Multi / single select list |
| `AppProgressView` | Progress indicator |
| `EmptyView` | Empty state |
| `PressTapModifier` | Ripple-suppressed press feedback |

**Shared enums** (`AppUIEntities`): `ActivityType`, `BackgroundType`, `AccessType`, `RequestType`

---

## Feature Module Internal Structure

Every feature follows identical layered structure:

```
appFeatures/<feature>/src/main/java/com/bonjur/<feature>/
├── di/
│   ├── DataModule.kt           ← @Binds DataSource impl → interface
│   └── <Feature>Module.kt      ← additional DI (if needed)
├── data/
│   ├── DTOs/<Feature>DTO.kt    ← @Serializable request/response models
│   ├── dataSource/
│   │   ├── <Feature>DataSource.kt       ← interface
│   │   └── <Feature>DataSourceImpl.kt   ← extends NetworkService
│   └── endPoints/<Feature>Endpoints.kt  ← sealed class : AppEndpoint
├── domain/
│   ├── models/<Feature>Models.kt        ← business models
│   └── useCase/
│       ├── <Feature>UseCase.kt          ← interface
│       └── <Feature>UseCaseImpl.kt      ← @Inject + DataSource dep
├── navigation/
│   ├── <Feature>Screens.kt              ← sealed navigation routes
│   └── <Feature>NavGraph.kt             ← NavGraphBuilder extension
└── presentation/
    └── <ScreenName>/
        ├── <ScreenName>Screen.kt        ← @Composable
        ├── <ScreenName>ViewModel.kt     ← @HiltViewModel : FeatureViewModel
        └── models/<ScreenName>Model.kt  ← ViewState / Action / SideEffect / InputData
```

---

## Feature Overview

### Auth

**Screens**: Onboarding → ChooseUniversity → SignIn → Welcome → OptionalInfo

**SignIn flow**:
```
SignInAction.Login(email, password)
  → useCase.login(communityId, email, password)
    → dataSource.login(LoginRequest)
      → POST /auth/login
    ← LoginResponse { accessToken, refreshToken, userId, isFirstLogin }
  → TokenManager.save tokens
  → if isFirstLogin: SideEffect.NavigateToWelcome
  → else: SideEffect.NavigateToMain
```

**OptionalInfo**: multipart PATCH `/auth/optionals` (photo, bio, birthday, interests, gender, language)

---

### Discover

**Screens**: DiscoverScreen (search hub)  
**Dependencies**: clubs, events, hangouts, communities

---

### Clubs

**Screens**: List → Details → Create/Edit

**Key endpoints** (`ClubsEndpoints`):
```
GET    /api/cs/v1/clubs              (paginated, search, category filter)
GET    /api/cs/v1/clubs/{id}
POST   /api/cs/v1/clubs              (multipart: logo + cover)
PUT    /api/cs/v1/clubs/{id}
POST   /api/cs/v1/clubs/{id}/join
```

**State machine example** (ClubsListModel):
```kotlin
data class ClubsListViewState(
    val clubs: List<ClubCardUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val searchText: String = ""
) : FeatureState

sealed interface ClubsListAction : FeatureAction {
    data object FetchData : ClubsListAction
    data object LoadMore : ClubsListAction
    data class SearchChanged(val text: String) : ClubsListAction
    data class ItemTapped(val id: Int) : ClubsListAction
}

sealed interface ClubsListSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : ClubsListSideEffect
    data class Error(val error: ApiException) : ClubsListSideEffect
    data class NavigateToDetails(val id: Int) : ClubsListSideEffect
}
```

---

### Communities

**Screens**: CommunityDetail, FacultyBrowse, FacultySelection, FacultyStudentList  
⚠ Not yet wired into `MainScreen` navigation — navigation route missing

---

### Events

**Screens**: EventsList → EventDetails → EventCreate  
**Shared with**: Clubs (events live inside clubs)

---

### Hangouts

**Screens**: HangoutList → HangoutDetails → HangoutCreate

---

### Groups

**Screens**: GroupsList  
**Dependencies**: clubs, events, hangouts

---

### Profile

**Screens**: ProfileDetail, EditProfile, StudentCard, ProfileSettings  
⚠ Not yet wired into `MainScreen` navigation

---

## Build Config

| Property | Value |
|---|---|
| AGP | 8.13.1 |
| Kotlin | 2.0.21 |
| Compose BOM | 2024.09.00 |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 36 (Android 15) |
| JVM target | 11 |
| DI | Hilt 2.57.2 |
| HTTP | Ktor (Android engine) |
| JSON | kotlinx.serialization |
| Images | Coil 2.6.0 |
| Security | androidx.security EncryptedSharedPreferences |
| Splash | SplashScreen 1.0.1 |

---

## Key Files Quick-Reference

| Purpose | Path |
|---|---|
| App entry | `app/src/main/java/com/bonjur/app/MainActivity.kt` |
| Application class | `app/src/main/java/com/bonjur/app/MainApplication.kt` |
| Root nav host | `app/src/main/java/com/bonjur/app/navigation/AppNavigation.kt` |
| Tab bar | `app/src/main/java/com/bonjur/app/tabBar/AppTabBar.kt` |
| Base ViewModel | `appCore/appFoundation/src/main/java/com/bonjur/appfoundation/FeatureViewModel.kt` |
| Feature store | `appCore/appFoundation/src/main/java/com/bonjur/appfoundation/FeatureStore.kt` |
| Navigator | `appCore/navigation/src/main/java/com/bonjur/navigation/Navigator.kt` |
| App screens | `appCore/navigation/src/main/java/com/bonjur/navigation/AppScreens.kt` |
| NavArgs store | `appCore/navigation/src/main/java/com/bonjur/navigation/NavArgs.kt` |
| API client | `appCore/network/src/main/java/com/bonjur/network/APIClient/APIClient.kt` |
| Token manager | `appCore/network/src/main/java/com/bonjur/network/manager/TokenManager.kt` |
| Secure storage | `appCore/storage/src/main/java/com/bonjur/storage/securePreference/SecureStorageImpl.kt` |
| Theme | `appCore/designSystem/src/main/java/com/bonjur/designSystem/ui/theme/BonjurTheme.kt` |
| Deps catalog | `gradle/libs.versions.toml` |

---

## Known Issues / In-Progress

1. **Profile + Communities not in `MainScreen`** — navigation routes exist in feature modules but not registered in root `AppNavigation.kt`
2. **NavArgs process-death risk** — in-memory store; survives config change but not process kill
3. **No offline cache** — no Room DB; all data from network, only tokens persisted
4. **Feature cross-dependencies** — some features depend on sibling feature modules (e.g. clubs → events), creating potential coupling
