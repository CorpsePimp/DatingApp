package com.example.datingapp.domain.model

data class User(
    val id: String,
    val name: String,
    val age: Int,
    val bio: String,
    val photoUrl: String? = null,
    val distance: Int? = null,
    val interests: List<String> = emptyList()
)

// Dummy data for testing
fun getDummyUsers(): List<User> = listOf(
    User(
        id = "1",
        name = "Анна",
        age = 25,
        bio = "Люблю путешествовать и фотографировать закаты 🌅",
        distance = 2,
        interests = listOf("Путешествия", "Фотография", "Кофе")
    ),
    User(
        id = "2",
        name = "Мария",
        age = 23,
        bio = "Танцую сальсу, обожаю латиноамериканскую кухню 💃",
        distance = 5,
        interests = listOf("Танцы", "Кулинария", "Музыка")
    ),
    User(
        id = "3",
        name = "Елена",
        age = 27,
        bio = "Книжный червь и любительница котиков 📚🐱",
        distance = 3,
        interests = listOf("Чтение", "Животные", "Кино")
    ),
    User(
        id = "4",
        name = "Дарья",
        age = 24,
        bio = "Фитнес-тренер, ЗОЖ и позитив ✨",
        distance = 1,
        interests = listOf("Фитнес", "ЗОЖ", "Йога")
    ),
    User(
        id = "5",
        name = "Виктория",
        age = 26,
        bio = "Художница, ищу музу и вдохновение 🎨",
        distance = 4,
        interests = listOf("Искусство", "Живопись", "Галереи")
    ),
    User(
        id = "6",
        name = "Ольга",
        age = 28,
        bio = "Любительница активного отдыха и гор 🏔️",
        distance = 6,
        interests = listOf("Горы", "Туризм", "Природа")
    ),
    User(
        id = "7",
        name = "Екатерина",
        age = 22,
        bio = "Студентка, меломан, люблю концерты 🎵",
        distance = 2,
        interests = listOf("Музыка", "Концерты", "Учеба")
    ),
    User(
        id = "8",
        name = "Анастасия",
        age = 29,
        bio = "Маркетолог и travel blogger 🌍",
        distance = 7,
        interests = listOf("Маркетинг", "Блоггинг", "Путешествия")
    ),
    User(
        id = "9",
        name = "Полина",
        age = 24,
        bio = "Шеф-повар, готовлю с любовью ❤️🍝",
        distance = 3,
        interests = listOf("Кулинария", "Рестораны", "Гастрономия")
    ),
    User(
        id = "10",
        name = "София",
        age = 25,
        bio = "Психолог, интересуюсь людьми и их историями 🧠",
        distance = 4,
        interests = listOf("Психология", "Общение", "Развитие")
    )
)
