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
        photoUrl = "https://picsum.photos/seed/user1/400/600",
        distance = 2,
        interests = listOf("Путешествия", "Фотография", "Кофе")
    ),
    User(
        id = "2",
        name = "Мария",
        age = 23,
        bio = "Танцую сальсу, обожаю латиноамериканскую кухню 💃",
        photoUrl = "https://picsum.photos/seed/user2/400/600",
        distance = 5,
        interests = listOf("Танцы", "Кулинария", "Музыка")
    ),
    User(
        id = "3",
        name = "Елена",
        age = 27,
        bio = "Книжный червь и любительница котиков 📚🐱",
        photoUrl = "https://picsum.photos/seed/user3/400/600",
        distance = 3,
        interests = listOf("Чтение", "Животные", "Кино")
    ),
    User(
        id = "4",
        name = "Дарья",
        age = 24,
        bio = "Фитнес-тренер, ЗОЖ и позитив ✨",
        photoUrl = "https://picsum.photos/seed/user4/400/600",
        distance = 1,
        interests = listOf("Фитнес", "ЗОЖ", "Йога")
    ),
    User(
        id = "5",
        name = "Виктория",
        age = 26,
        bio = "Художница, ищу музу и вдохновение 🎨",
        photoUrl = "https://picsum.photos/seed/user5/400/600",
        distance = 4,
        interests = listOf("Искусство", "Живопись", "Галереи")
    ),
    User(
        id = "6",
        name = "Ольга",
        age = 28,
        bio = "Любительница активного отдыха и гор 🏔️",
        photoUrl = "https://picsum.photos/seed/user6/400/600",
        distance = 6,
        interests = listOf("Горы", "Туризм", "Природа")
    ),
    User(
        id = "7",
        name = "Екатерина",
        age = 22,
        bio = "Студентка, меломан, люблю концерты 🎵",
        photoUrl = "https://picsum.photos/seed/user7/400/600",
        distance = 2,
        interests = listOf("Музыка", "Концерты", "Учеба")
    ),
    User(
        id = "8",
        name = "Анастасия",
        age = 29,
        bio = "Маркетолог и travel blogger 🌍",
        photoUrl = "https://picsum.photos/seed/user8/400/600",
        distance = 7,
        interests = listOf("Маркетинг", "Блоггинг", "Путешествия")
    ),
    User(
        id = "9",
        name = "Полина",
        age = 24,
        bio = "Шеф-повар, готовлю с любовью ❤️🍝",
        photoUrl = "https://picsum.photos/seed/user9/400/600",
        distance = 3,
        interests = listOf("Кулинария", "Рестораны", "Гастрономия")
    ),
    User(
        id = "10",
        name = "София",
        age = 25,
        bio = "Психолог, интересуюсь людьми и их историями 🧠",
        photoUrl = "https://picsum.photos/seed/user10/400/600",
        distance = 4,
        interests = listOf("Психология", "Общение", "Развитие")
    )
)

fun getItDummyUsers(): List<User> = listOf(
    User(
        id = "it_1",
        name = "Артем",
        age = 27,
        bio = "Android-разработчик, люблю Jetpack Compose и архитектуру приложений.",
        photoUrl = "https://picsum.photos/seed/it1/400/600",
        distance = 3,
        interests = listOf("Kotlin", "Android", "Clean Architecture")
    ),
    User(
        id = "it_2",
        name = "Даниил",
        age = 29,
        bio = "Бэкенд-инженер на Kotlin/Spring, вечерами делаю pet-проекты.",
        photoUrl = "https://picsum.photos/seed/it2/400/600",
        distance = 5,
        interests = listOf("Kotlin", "Spring", "PostgreSQL")
    ),
    User(
        id = "it_3",
        name = "Илья",
        age = 25,
        bio = "Фронтенд и React, ищу команду для SaaS-стартапа.",
        photoUrl = "https://picsum.photos/seed/it3/400/600",
        distance = 2,
        interests = listOf("React", "TypeScript", "Стартапы")
    ),
    User(
        id = "it_4",
        name = "Никита",
        age = 30,
        bio = "DevOps: автоматизирую CI/CD и оптимизирую инфраструктуру.",
        photoUrl = "https://picsum.photos/seed/it4/400/600",
        distance = 4,
        interests = listOf("DevOps", "Kubernetes", "AWS")
    ),
    User(
        id = "it_5",
        name = "Роман",
        age = 26,
        bio = "Инженер данных: пайплайны и ML для продакшена.",
        photoUrl = "https://picsum.photos/seed/it5/400/600",
        distance = 6,
        interests = listOf("Python", "Данные", "ML")
    ),
    User(
        id = "it_6",
        name = "Сергей",
        age = 28,
        bio = "Fullstack с продуктовым мышлением: коллаборации и ревью кода.",
        photoUrl = "https://picsum.photos/seed/it6/400/600",
        distance = 1,
        interests = listOf("Node.js", "Продукт", "Менторство")
    )
)
