# 🤖 AI Context: Dating App Project

**Дата последнего обновления**: 2026-02-14  
**Цель файла**: Быстрое восстановление контекста для AI без траты токенов на исследование кодовой базы

---

## 📋 КРАТКАЯ СВОДКА ПРОЕКТА

### Тип приложения
Android Dating App (аналог Tinder) на Kotlin + Jetpack Compose

### Текущий статус
- ✅ Архитектура MVVM создана
- ✅ Экраны авторизации (Login, Register) — готовы
- ✅ Главный экран со свайпами карточек — готов
- ✅ Интеграция Coil для загрузки изображений — работает
- ✅ Нижняя навигация (5 кнопок) — реализована
- ✅ Экраны Чаты и Профиль — полностью реализованы
- ✅ Экран Карточки (Activities) — Bento Grid + рекламные блоки
- ✅ **Система чатов с AI** — OpenRouter API интеграция
- ✅ **Редактирование профиля** — 4-шаговый Wizard
- ⏳ Экран: Лайки — заглушка

### Дизайн-система
- **Стиль**: Modern Glassmorphism / Soft UI (светлая тема)
- **Цвета**: Light Pink (#FFF5F7) → Light Violet (#F5F0FF) → Accent Pink (#E91E63)
- **Скругления**: 24-32dp для карточек, 16dp для кнопок
- **Типографика**: Material 3
- **Иконки**: Material Icons Extended

---

## 🗂️ СТРУКТУРА ПРОЕКТА

```
app/src/main/java/com/example/datingapp/
├── data/
│   ├── local/              # (пусто)
│   ├── remote/
│   │   └── OpenRouterApiService.kt  # ✅ AI Chat API (Ktor + OkHttp)
│   └── repository/
│       ├── ProfileRepository.kt     # ✅ Singleton StateFlow<UserProfile>
│       └── ChatRepository.kt        # ✅ Session-based chat history
│
├── domain/
│   ├── model/
│   │   ├── User.kt                  # ✅ Модель + getDummyUsers()
│   │   ├── UserProfile.kt           # ✅ Профиль + 50+ AvailableInterests
│   │   └── ChatMessage.kt           # ✅ Модель сообщения + ChatContacts
│   └── ...
│
└── presentation/
    ├── navigation/
    │   └── NavGraph.kt              # ✅ Главная навигация (login/register/main)
    │
    ├── ui/
    │   ├── auth/
    │   │   ├── login/LoginScreen.kt
    │   │   └── register/RegisterScreen.kt
    │   │
    │   ├── cards/
    │   │   ├── ActivitiesScreen.kt      # ✅ Bento Grid + рекламные блоки
    │   │   └── AdBannerComponent.kt
    │   │
    │   ├── chat/
    │   │   ├── ChatListScreen.kt        # ✅ Список чатов + новые пары
    │   │   └── ChatDetailScreen.kt      # ✅ Экран чата с AI
    │   │
    │   ├── components/
    │   │   └── BottomNavigationBar.kt   # ✅ 5 кнопок
    │   │
    │   ├── main/
    │   │   ├── MainScaffold.kt          # ✅ Bottom nav + роутинг
    │   │   └── home/
    │   │       ├── MainScreen.kt        # ✅ Свайп-экран
    │   │       └── SwipeableCard.kt     # ✅ Карточка с анимациями
    │   │
    │   ├── profile/
    │   │   ├── ProfileScreen.kt         # ✅ Профиль + Bento Grid
    │   │   └── wizard/                  # ✅ 4-шаговый Wizard редактирования
    │   │       ├── EditProfileWizard.kt
    │   │       └── StepComponents.kt
    │   │
    │   └── theme/
    │       └── Theme.kt
    │
    └── viewmodel/
        ├── ProfileViewModel.kt          # ✅ Wizard state + DraftProfile
        └── ChatViewModel.kt             # ✅ Chat logic + AI integration
```

---

## 🆕 НОВЫЕ КОМПОНЕНТЫ (с последнего обновления)

### 1. Система редактирования профиля (4-шаговый Wizard)

**Файлы:**
- `presentation/viewmodel/ProfileViewModel.kt` — WizardStep enum, DraftProfile state
- `presentation/ui/profile/wizard/EditProfileWizard.kt` — главный контейнер
- `presentation/ui/profile/wizard/StepComponents.kt` — UI для каждого шага

**Шаги Wizard:**
1. **BASIC_INFO** — Name*, City*, Bio*, Occupation (валидация: все required не пустые)
2. **PHOTOS** — 2x3 grid + HorizontalPager preview (валидация: >= 1 фото)
3. **INTERESTS** — 50+ интересов, поиск, FlowRow (валидация: >= 1 выбран)
4. **PREVIEW** — Tinder-style карточка + кнопки "Изменить" / "Сохранить"

**Ключевые классы:**
```kotlin
enum class WizardStep { BASIC_INFO, PHOTOS, INTERESTS, PREVIEW }

data class DraftProfile(
    val name: String, val city: String, val bio: String,
    val occupation: String, val photoUrls: List<String>,
    val selectedInterests: List<String>
) {
    val isStep1Valid: Boolean  // name, city, bio не пустые
    val isStep2Valid: Boolean  // photoUrls.isNotEmpty()
    val isStep3Valid: Boolean  // selectedInterests.isNotEmpty()
}

data class EditProfileWizardState(
    val currentStep: WizardStep,
    val draft: DraftProfile,
    val interestSearchQuery: String,
    val isSaving: Boolean,
    val previewPhotoIndex: Int
)
```

---

### 2. Система чатов с AI (OpenRouter API)

**Файлы:**
- `data/remote/OpenRouterApiService.kt` — Ktor client + fallback models
- `data/repository/ChatRepository.kt` — session storage (Map<userId, List<Message>>)
- `domain/model/ChatMessage.kt` — модели + 5 предустановленных ChatContacts
- `presentation/viewmodel/ChatViewModel.kt` — UI state + API calls
- `presentation/ui/chat/ChatDetailScreen.kt` — UI чата

**Особенности:**
- Bottom nav скрывается при открытии чата (`chat_detail/{userId}`)
- Аватар в TopBar справа, кнопка "назад" слева
- Typing indicator с анимацией точек
- Fallback ответы если API недоступен
- SSL проверка отключена (для dev эмулятора с неправильной датой)

**API конфигурация:**
```kotlin
object OpenRouterApiService {
    private const val API_KEY = "sk-or-v1-..."  // В коде напрямую
    private const val BASE_URL = "https://openrouter.ai/api/v1/chat/completions"
    
    // Fallback модели (пробует по очереди):
    private val FREE_MODELS = listOf(
        "mistralai/mistral-7b-instruct:free",
        "openchat/openchat-7b:free",
        "huggingfaceh4/zephyr-7b-beta:free",
        "nousresearch/nous-capybara-7b:free"
    )
}
```

**⚠️ Проблема:** API возвращает 404 на бесплатные модели. Пока используются fallback ответы.

---

### 3. ChatContacts (предустановленные персонажи)

```kotlin
object ChatContacts {
    val contacts = listOf(
        ChatContact(id="1", name="Анастасия", systemPrompt="Привет! Я увидела..."),
        ChatContact(id="2", name="Виктория", ...),
        ChatContact(id="3", name="Екатерина", ...),
        ChatContact(id="4", name="Полина", ...),
        ChatContact(id="5", name="Александра", ...)
    )
}
```

---

## 📱 НАВИГАЦИЯ

### Внутренняя навигация MainScaffold
```kotlin
"chat"              → ChatListScreen
"chat_detail/{id}"  → ChatDetailScreen (bottom nav скрыт!)
"likes"             → PlaceholderScreen
"main"              → MainScreen (стартовый)
"cards"             → ActivitiesScreen
"profile"           → ProfileScreen
"edit_profile"      → EditProfileWizard
```

---

## 🔧 ЗАВИСИМОСТИ

```kotlin
// Ktor для API
implementation("io.ktor:ktor-client-core:2.3.7")
implementation("io.ktor:ktor-client-okhttp:2.3.7")
implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")

// Serialization
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
kotlin("plugin.serialization") version "2.0.21"

// Coil для изображений
implementation(libs.coil.compose)

// Material Icons Extended
implementation(libs.compose.material.icons.extended)
```

---

## 📊 МЕТРИКИ ПРОЕКТА

- **Всего файлов Kotlin**: ~20
- **Экранов**: 9 (8 готовых, 1 заглушка - Лайки)
- **Компонентов**:
  - Auth: LoginScreen, RegisterScreen
  - Main: MainScreen, SwipeableCard
  - Chat: ChatListScreen, ChatDetailScreen
  - Profile: ProfileScreen, EditProfileWizard, StepComponents
  - Cards: ActivitiesScreen, AdBannerComponent
  - Data: OpenRouterApiService, ChatRepository, ProfileRepository
  - ViewModels: ProfileViewModel, ChatViewModel

---

## ⚠️ ИЗВЕСТНЫЕ ПРОБЛЕМЫ

1. **OpenRouter API 404** — бесплатные модели недоступны, используется fallback
2. **SSL на эмуляторе** — отключена проверка сертификатов из-за неверной даты
3. **Свайп на эмуляторе** — карточка не всегда возвращается (работает на реальном устройстве)

---

## 🎯 СЛЕДУЮЩИЕ ШАГИ

1. Починить OpenRouter API или найти альтернативу
2. Реализовать экран "Лайки"
3. Добавить экран настроек
4. Добавить темную тему
5. Реализовать реальную авторизацию
