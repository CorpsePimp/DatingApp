package com.example.datingapp.data.repository

import com.example.datingapp.domain.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Singleton repository for chat messages
 * Stores messages in memory during app session
 */
object ChatRepository {

    // Map of userId to list of messages
    private val chatHistory: MutableMap<String, MutableList<ChatMessage>> = mutableMapOf()

    // StateFlow for observing messages per chat
    private val _chatFlows: MutableMap<String, MutableStateFlow<List<ChatMessage>>> = mutableMapOf()

    /**
     * Get messages flow for a specific user
     */
    fun getMessagesFlow(userId: String): StateFlow<List<ChatMessage>> {
        if (!_chatFlows.containsKey(userId)) {
            _chatFlows[userId] = MutableStateFlow(chatHistory[userId]?.toList() ?: emptyList())
        }
        return _chatFlows[userId]!!.asStateFlow()
    }

    /**
     * Get current messages for a user
     */
    fun getMessages(userId: String): List<ChatMessage> {
        return chatHistory[userId]?.toList() ?: emptyList()
    }

    /**
     * Check if chat has any messages
     */
    fun hasMessages(userId: String): Boolean {
        return chatHistory[userId]?.isNotEmpty() == true
    }

    /**
     * Add a message to chat history
     */
    fun addMessage(userId: String, message: ChatMessage) {
        if (!chatHistory.containsKey(userId)) {
            chatHistory[userId] = mutableListOf()
        }
        chatHistory[userId]!!.add(message)

        // Update flow
        _chatFlows[userId]?.update { chatHistory[userId]!!.toList() }
    }

    /**
     * Add initial system message (greeting from AI)
     */
    fun initializeChatWithGreeting(userId: String, greeting: String) {
        if (!hasMessages(userId)) {
            addMessage(
                userId,
                ChatMessage(
                    content = greeting,
                    isFromUser = false
                )
            )
        }
    }

    /**
     * Get last N messages for context
     */
    fun getLastMessages(userId: String, count: Int = 10): List<ChatMessage> {
        val messages = chatHistory[userId] ?: return emptyList()
        return messages.takeLast(count)
    }

    /**
     * Clear chat history for a user
     */
    fun clearChat(userId: String) {
        chatHistory.remove(userId)
        _chatFlows[userId]?.update { emptyList() }
    }

    /**
     * Clear all chats
     */
    fun clearAll() {
        chatHistory.clear()
        _chatFlows.values.forEach { it.update { emptyList() } }
    }
}
