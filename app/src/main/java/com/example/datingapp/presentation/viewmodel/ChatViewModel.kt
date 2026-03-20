package com.example.datingapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datingapp.data.remote.OpenRouterApiService
import com.example.datingapp.data.repository.ChatRepository
import com.example.datingapp.domain.model.ChatContact
import com.example.datingapp.domain.model.ChatContacts
import com.example.datingapp.domain.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI State for Chat Detail Screen
 */
data class ChatDetailUiState(
    val contact: ChatContact? = null,
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isTyping: Boolean = false,
    val isLoading: Boolean = true
)

/**
 * ViewModel for Chat Detail Screen
 */
class ChatViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChatDetailUiState())
    val uiState: StateFlow<ChatDetailUiState> = _uiState.asStateFlow()

    private var currentUserId: String? = null

    /**
     * Initialize chat with a specific user
     */
    fun initChat(userId: String) {
        currentUserId = userId

        val contact = ChatContacts.getById(userId)

        // Initialize chat with greeting if no messages exist
        if (contact != null && !ChatRepository.hasMessages(userId)) {
            ChatRepository.initializeChatWithGreeting(userId, contact.systemPrompt)
        }

        // Load messages
        val messages = ChatRepository.getMessages(userId)

        _uiState.update {
            it.copy(
                contact = contact,
                messages = messages,
                isLoading = false
            )
        }

        // Observe messages
        viewModelScope.launch {
            ChatRepository.getMessagesFlow(userId).collect { newMessages ->
                _uiState.update { it.copy(messages = newMessages) }
            }
        }
    }

    /**
     * Update input text
     */
    fun updateInputText(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    /**
     * Send a message
     */
    fun sendMessage() {
        val userId = currentUserId ?: return
        val text = _uiState.value.inputText.trim()

        if (text.isBlank()) return

        // Add user message
        val userMessage = ChatMessage(
            content = text,
            isFromUser = true
        )
        ChatRepository.addMessage(userId, userMessage)

        // Clear input and show typing indicator
        _uiState.update {
            it.copy(
                inputText = "",
                isTyping = true
            )
        }

        // Fetch AI response
        viewModelScope.launch {
            val history = ChatRepository.getLastMessages(userId, 10)
            val contactName = _uiState.value.contact?.name ?: "Девушка"

            val result = OpenRouterApiService.fetchAIResponse(history, contactName)

            result.onSuccess { response ->
                val aiMessage = ChatMessage(
                    content = response,
                    isFromUser = false
                )
                ChatRepository.addMessage(userId, aiMessage)
            }

            _uiState.update { it.copy(isTyping = false) }
        }
    }
}
