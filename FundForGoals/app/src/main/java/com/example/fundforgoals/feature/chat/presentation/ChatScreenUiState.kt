package com.example.fundforgoals.feature.chat.presentation

import com.example.fundforgoals.supabase.model.Chat
import com.example.fundforgoals.supabase.model.Chatroom

data class ChatUiState(
    val currentUser: String = "",
    val searchQuery: String = "",
    val selectedConversationId: String? = null,
    val input: String = "",
    val chatroom: List<Chatroom> = emptyList(),
    val chats: List<Chat> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)