package com.example.datingapp.domain.model

/**
 * Chat message model
 */
data class ChatMessage(
    val id: String = System.currentTimeMillis().toString(),
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Chat contact/match info
 */
data class ChatContact(
    val id: String,
    val name: String,
    val photoUrl: String,
    val isOnline: Boolean = false,
    val isVerified: Boolean = false,
    val systemPrompt: String = "" // Initial greeting/context for AI
)

/**
 * Predefined chat contacts with personalities
 */
object ChatContacts {
    val contacts = listOf(
        ChatContact(
            id = "1",
            name = "Анастасия",
            photoUrl = "https://picsum.photos/seed/chat1/200/200",
            isOnline = true,
            isVerified = true,
            systemPrompt = "Привет! Я Анастасия 💕 Увидела, что тебе тоже нравится фитнес. Какой твой любимый вид тренировок?"
        ),
        ChatContact(
            id = "2",
            name = "Виктория",
            photoUrl = "https://picsum.photos/seed/chat2/200/200",
            isOnline = true,
            isVerified = false,
            systemPrompt = "Привет! Я Вика 😊 Заметила, что ты из IT. Я тоже программист! Что изучаешь сейчас?"
        ),
        ChatContact(
            id = "3",
            name = "Екатерина",
            photoUrl = "https://picsum.photos/seed/chat3/200/200",
            isOnline = false,
            isVerified = true,
            systemPrompt = "Привет! Я Катя ✨ Люблю путешествовать и пробовать новую кухню. Какое твоё любимое место?"
        ),
        ChatContact(
            id = "4",
            name = "Полина",
            photoUrl = "https://picsum.photos/seed/chat4/200/200",
            isOnline = false,
            isVerified = false,
            systemPrompt = "Привет! Я Полина 🎨 Обожаю искусство и музыку. Слышала, ты тоже творческий человек?"
        ),
        ChatContact(
            id = "5",
            name = "Александра",
            photoUrl = "https://picsum.photos/seed/chat5/200/200",
            isOnline = false,
            isVerified = true,
            systemPrompt = "Привет! Я Саша 📚 Книжный червь и кофеман. Что читаешь сейчас?"
        )
    )

    val itContacts = listOf(
        ChatContact(
            id = "it_1",
            name = "Михаил",
            photoUrl = "https://picsum.photos/seed/it_chat1/200/200",
            isOnline = true,
            isVerified = true,
            systemPrompt = "Привет! Я прогнал твой PR, хочешь вместе пройтись по замечаниям и улучшить архитектуру?"
        ),
        ChatContact(
            id = "it_2",
            name = "Георгий",
            photoUrl = "https://picsum.photos/seed/it_chat2/200/200",
            isOnline = true,
            isVerified = true,
            systemPrompt = "Есть баг в проде: падает авторизация после рефреша токена. Поможешь локализовать причину?"
        ),
        ChatContact(
            id = "it_3",
            name = "Павел",
            photoUrl = "https://picsum.photos/seed/it_chat3/200/200",
            isOnline = false,
            isVerified = false,
            systemPrompt = "Собираю мини-команду для AI-стартапа в edtech. Интересно обсудить идею?"
        ),
        ChatContact(
            id = "it_4",
            name = "Влад",
            photoUrl = "https://picsum.photos/seed/it_chat4/200/200",
            isOnline = false,
            isVerified = true,
            systemPrompt = "Привет! Давай устроим созвон и сделаем peer-review твоего pet-проекта."
        ),
        ChatContact(
            id = "it_5",
            name = "Тимур",
            photoUrl = "https://picsum.photos/seed/it_chat5/200/200",
            isOnline = true,
            isVerified = false,
            systemPrompt = "Ищу напарника на хакатон. Могу взять backend и деплой, ты за frontend?"
        )
    )

    fun getById(id: String): ChatContact? = (contacts + itContacts).find { it.id == id }
}
