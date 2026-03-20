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

    fun getById(id: String): ChatContact? = contacts.find { it.id == id }
}
