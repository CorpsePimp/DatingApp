package com.example.datingapp.domain.model

/**
 * User profile data model
 */
data class UserProfile(
    val id: String = "1",
    val name: String = "",
    val occupation: String = "",
    val bio: String = "",
    val city: String = "",
    val photoUrls: List<String> = emptyList(),
    val interests: List<String> = emptyList(),
    val profileCompletion: Float = 0f
) {
    /**
     * Calculate profile completion percentage
     */
    fun calculateCompletion(): Float {
        var score = 0f
        if (name.isNotBlank()) score += 0.2f
        if (occupation.isNotBlank()) score += 0.15f
        if (bio.isNotBlank()) score += 0.2f
        if (city.isNotBlank()) score += 0.15f
        if (photoUrls.isNotEmpty()) score += 0.15f
        if (interests.isNotEmpty()) score += 0.15f
        return score.coerceAtMost(1f)
    }
}

/**
 * Available interests for selection (50+ items, sorted by popularity)
 */
object AvailableInterests {
    val all = listOf(
        "Фитнес", "Путешествия", "Кино", "Музыка", "Фотография",
        "Готовка", "Чтение", "Игры", "Спорт", "Искусство",
        "Танцы", "Йога", "Походы", "Кофе", "Вино",
        "Мода", "Собаки", "Кошки", "Природа", "Пляж",
        "Netflix", "Концерты", "Гастрономия", "Бег", "Зал",
        "Плавание", "Велосипед", "Медитация", "Волонтёрство", "Писательство",
        "Поэзия", "Театр", "Комедии", "Караоке", "Настольные игры",
        "Кемпинг", "Рыбалка", "Гольф", "Теннис", "Баскетбол",
        "Аниме", "Манга", "K-Pop", "Астрология", "Тату",
        "Винтаж", "Кроссовки", "Крипто", "Инвестиции", "Трейдинг",
        "Ноотропы", "Биохакинг", "Стартапы", "Программирование", "AI",
        "VR", "Киберспорт", "Косплей", "LARP", "DnD"
    )
}
