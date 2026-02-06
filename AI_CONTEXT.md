# 🤖 AI Context: Dating App Project

**Дата последнего обновления**: 2026-02-06  
**Цель файла**: Быстрое восстановление контекста для AI без траты токенов на исследование кодовой базы

---

## 📋 КРАТКАЯ СВОДКА ПРОЕКТА

### Тип приложения
Android Dating App (аналог Tinder) на Kotlin + Jetpack Compose

### Текущий статус
- ✅ Архитектура MVVM создана (пустые директории)
- ✅ Экраны авторизации (Login, Register) — готовы
- ✅ Главный экран со свайпами карточек — готов
- ✅ Интеграция Coil для загрузки изображений — работает
- ✅ Нижняя навигация (5 кнопок) — реализована
- ⏳ Экраны: Чаты, Лайки, Карточки, Профиль — заглушки

### Дизайн-система
- **Стиль**: Modern Glassmorphism / Soft UI
- **Цвета**: Soft Pink (#FFE5EC) → Light Violet (#E5D9F2) → Deep Pink (#E91E63)
- **Скругления**: 24-32dp для карточек, 16dp для кнопок
- **Типографика**: Material 3

---

## 🗂️ СТРУКТУРА ПРОЕКТА

```
app/src/main/java/com/example/datingapp/
├── data/
│   ├── local/          # (пусто)
│   ├── remote/         # (пусто)
│   └── repository/     # (пусто)
│
├── domain/
│   ├── model/
│   │   └── User.kt     # ✅ Модель пользователя + getDummyUsers()
│   ├── repository/     # (пусто)
│   └── usecase/        # (пусто)
│
└── presentation/
    ├── navigation/
    │   └── NavGraph.kt             # ✅ Главная навигация
    │
    ├── ui/
    │   ├── auth/
    │   │   ├── login/
    │   │   │   └── LoginScreen.kt      # ✅ Email/Password + VK/Mail.ru
    │   │   └── register/
    │   │       └── RegisterScreen.kt   # ✅ Email/Password + социальные кнопки
    │   │
    │   ├── components/
    │   │   └── BottomNavigationBar.kt  # ✅ Нижняя панель (5 кнопок)
    │   │
    │   ├── main/
    │   │   ├── MainScaffold.kt         # ✅ Обертка с bottom nav
    │   │   └── home/
    │   │       ├── MainScreen.kt       # ✅ Свайп-экран
    │   │       └── SwipeableCard.kt    # ✅ Карточка с фото, свайпом, анимациями
    │   │
    │   └── theme/
    │       └── Theme.kt                 # ✅ Material 3 + кастомные цвета
    │
    └── viewmodel/       # (пусто)
```

---

## 🎨 ДИЗАЙН-СИСТЕМА (Theme.kt)

### Цвета
```kotlin
val SoftPink = Color(0xFFFFE5EC)
val LightViolet = Color(0xFFE5D9F2)
val DeepPink = Color(0xFFE91E63)
val BackgroundStart = Color(0xFFFFF5F7)
val BackgroundEnd = Color(0xFFF3E5F5)
val TextPrimary = Color(0xFF2C2C2C)
val TextSecondary = Color(0xFF7F7F7F)
val ErrorRed = Color(0xFFFF6B6B)
val SuccessGreen = Color(0xFF4CAF50)
val VKBlue = Color(0xFF0077FF)
val MailRuBlue = Color(0xFF168DE2)
```

### Скругления
- Карточки: `RoundedCornerShape(32.dp)`
- Кнопки: `RoundedCornerShape(16.dp)`
- Bottom Nav: `RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)`

---

## 📱 НАВИГАЦИОННАЯ СТРУКТУРА

### Главный NavGraph (AppNavGraph)
```kotlin
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")
    object Likes : Screen("likes")      // WARNING: не используется (в MainScaffold)
    object Cards : Screen("cards")      // WARNING: не используется (в MainScaffold)
    object Profile : Screen("profile")  // WARNING: не используется (в MainScaffold)
    object Chat : Screen("chat")        // WARNING: не используется (в MainScaffold)
}
```

### Роуты:
1. `/login` → `LoginScreen`
2. `/register` → `RegisterScreen`
3. `/main` → `MainScaffold` (содержит внутреннюю навигацию)

### Внутренняя навигация MainScaffold (Bottom Nav)
```kotlin
Роуты (строки, НЕ Screen objects):
- "chat"    → PlaceholderScreen("Чаты", "💬")
- "likes"   → PlaceholderScreen("Лайки", "❤️")
- "main"    → MainScreen (свайпы карточек) ← СТАРТОВЫЙ
- "cards"   → PlaceholderScreen("Карточки", "🎴")
- "profile" → PlaceholderScreen("Профиль", "👤")
```

---

## 🔑 КЛЮЧЕВЫЕ КОМПОНЕНТЫ

### 1. User.kt (domain/model/)
```kotlin
data class User(
    val id: String,
    val name: String,
    val age: Int,
    val bio: String,
    val photoUrl: String? = null,      // ✅ Picsum URLs добавлены
    val distance: Int? = null,
    val interests: List<String> = emptyList()
)

fun getDummyUsers(): List<User>  // 10 тестовых пользователей с фото
```

**Источник изображений**: `https://picsum.photos/seed/user{1-10}/400/600`

---

### 2. SwipeableCard.kt
**Ключевая логика свайпов**:

```kotlin
// Используется Animatable для плавного возврата
val animatedOffsetX = remember { Animatable(0f) }
val animatedOffsetY = remember { Animatable(0f) }

// Порог свайпа: 300f
onDragEnd = {
    when {
        offsetX > 300f -> onSwipeRight()  // LIKE
        offsetX < -300f -> onSwipeLeft()  // DISLIKE
        else -> {
            // Возврат в центр с spring анимацией
            coroutineScope.launch {
                animatedOffsetX.animateTo(0f, spring(...))
                animatedOffsetY.animateTo(0f, spring(...))
            }
        }
    }
}
```

**Загрузка изображений (Coil)**:
```kotlin
SubcomposeAsyncImage(
    model = user.photoUrl,
    contentScale = ContentScale.Crop,
    loading = { CircularProgressIndicator + gradient },
    error = { Icon(Person) + gradient }
)
```

---

### 3. BottomNavigationBar.kt
**Структура**:
```kotlin
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val iconSize: Dp = 24.dp
)

// 5 кнопок (слева направо):
1. MailOutline (Чаты)
2. Favorite (Лайки)
3. FavoriteBorder 32dp (Метч) ← ЦЕНТРАЛЬНАЯ, УВЕЛИЧЕННАЯ
4. Star (Карточки)
5. Person (Профиль)
```

**Визуальное состояние**:
- Активная: `DeepPink.copy(alpha = 0.15f)` фон, `DeepPink` иконка
- Неактивная: прозрачный фон, `TextSecondary` иконка

---

### 4. MainScreen.kt (home/)
**Изменения**:
- ❌ Убран `TopAppBar` (навигация теперь внизу)
- ✅ Карточки занимают весь экран
- ✅ Padding: `horizontal = 16.dp, vertical = 16.dp`

**Логика стека карточек**:
```kotlin
var currentIndex by remember { mutableIntStateOf(0) }
val users = remember { getDummyUsers().toMutableStateList() }

// Показываются 3 карточки для эффекта глубины:
- currentIndex + 2 (scale 0.9f, offset 16.dp)
- currentIndex + 1 (scale 0.95f, offset 8.dp)
- currentIndex (scale 1f, интерактивная)
```

**Кнопки действий под карточкой**:
```kotlin
ActionButtonsRow(
    onDislike = { currentIndex++ },
    onSuperLike = { /* TODO */ },
    onLike = { currentIndex++ }
)
```

---

### 5. LoginScreen.kt & RegisterScreen.kt
**Общие элементы**:
- Email/Password поля с валидацией
- Социальные кнопки: VK (#0077FF), Mail.ru (#168DE2)
- Glassmorphism стиль (градиенты + blur эффект)
- Анимации входа: `AnimatedVisibility` + `slideInVertically`

**Навигация**:
- Login → Register: `navController.navigate(Screen.Register.route)`
- Login/Register → Main: успешная авторизация

---

## 🔧 ЗАВИСИМОСТИ (build.gradle.kts)

### Текущие библиотеки:
```kotlin
// Core
implementation(libs.androidx.core.ktx)
implementation(libs.androidx.lifecycle.runtime.ktx)
implementation(libs.androidx.activity.compose)

// Compose
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.compose.ui)
implementation(libs.androidx.compose.ui.graphics)
implementation(libs.androidx.compose.ui.tooling.preview)
implementation(libs.androidx.compose.material3)

// Navigation
implementation(libs.androidxNavigationCompose)  // 2.9.7

// Image Loading
implementation(libs.coil.compose)  // 2.6.0
```

### libs.versions.toml:
```toml
[versions]
navigationCompose = "2.9.7"
coil = "2.6.0"
# ... другие версии
```

---

## ⚠️ ВАЖНЫЕ ДЕТАЛИ

### AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
**КРИТИЧНО**: без этого Coil не загружает изображения → crash

### Известные Warnings (не критичны):
1. `navController` в MainScaffold не используется (зарезервирован для будущего)
2. `onNavigateToProfile/Chat` в MainScreen не используются (навигация через bottom bar)
3. `Screen.Likes`, `Screen.Cards` не используются (внутри MainScaffold строковые роуты)
4. `isPressed = false` в ActionButton — LaunchedEffect сбрасывает

---

## 🎯 ЧТО ГОТОВО

### ✅ Полностью реализовано:
1. **Архитектура MVVM** (структура папок)
2. **Экраны авторизации** (Login, Register) с дизайном
3. **Главный экран свайпов** с карточками
4. **Загрузка изображений** через Coil (Picsum API)
5. **Нижняя навигация** (5 кнопок)
6. **Анимации**:
   - Плавный возврат карточки в центр
   - Индикаторы LIKE/NOPE при свайпе
   - Переходы между экранами

### ⏳ Заглушки (PlaceholderScreen):
- Чаты (💬)
- Лайки (❤️)
- Карточки (🎴)
- Профиль (👤)

---

## 🚀 СЛЕДУЮЩИЕ ШАГИ (для разработки)

### Приоритет 1: Основные экраны
1. **Экран профиля** (Profile)
   - Фото, имя, возраст, био
   - Редактирование данных
   - Настройки

2. **Экран лайков** (Likes)
   - Список людей, лайкнувших вас
   - Grid/List view карточек

3. **Экран чатов** (Chat)
   - Список мэтчей
   - Превью последних сообщений

### Приоритет 2: Функционал
- Реальный backend (Firebase/REST API)
- Настройки фильтров (возраст, расстояние)
- Push-уведомления
- Чат (сообщения в реальном времени)

### Приоритет 3: Улучшения
- Shimmer-эффект вместо CircularProgressIndicator
- Более реалистичные изображения (Unsplash API)
- Геолокация
- Верификация профиля

---

## 🐛 ИЗВЕСТНЫЕ ПРОБЛЕМЫ

### Решено:
- ✅ Crash при загрузке изображений → добавлен INTERNET permission
- ✅ Карточка не возвращалась в центр → использован Animatable вместо animateFloatAsState
- ✅ TopAppBar занимал место → удален, навигация перенесена вниз

### Текущие:
- Нет реальных данных (все mock)
- Placeholder экраны не реализованы
- Нет персистентности данных

---

## 💡 СОВЕТЫ ДЛЯ AI

### При добавлении новых экранов:
1. Создавай в `presentation/ui/{секция}/{экран}/`
2. Добавляй роут в `MainScaffold.kt` (строковый, не Screen object)
3. Обновляй `BottomNavItem` если нужна новая кнопка
4. Следуй дизайн-системе (Theme.kt цвета + скругления)

### При работе с навигацией:
- **Внешняя** (Login → Main): используй `Screen` sealed class
- **Внутренняя** (Bottom Nav): используй строковые роуты ("chat", "main", etc.)
- Не забывай `popUpTo` + `saveState` для bottom nav

### При работе с Compose:
- Всегда используй `remember` для состояний
- Для анимаций: `Animatable` > `animateFloatAsState` (больше контроля)
- Для списков: `LazyColumn` с `items()`, не `forEach`

### Стиль кода:
- Kotlin конвенции (camelCase, 4 пробела)
- `@Composable` функции с PascalCase
- Комментарии на русском (как в текущем коде)

---

## 📊 МЕТРИКИ ПРОЕКТА

- **Всего файлов Kotlin**: ~12
- **Строк кода**: ~2000+
- **Экранов**: 7 (3 готовых, 4 заглушки)
- **Компонентов**: 6 (LoginScreen, RegisterScreen, MainScreen, SwipeableCard, BottomNavigationBar, PlaceholderScreen)

---

**Конец контекста. При необходимости обновлять этот файл после значительных изменений.**
