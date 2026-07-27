package com.example.fundforgoals.feature.chat.presentation

data class ConversationUi(
    val id: String,
    val title: String,
    val subtitle: String,
    val date: String
)

data class ChatMessageUi(
    val id: String,
    val text: String,
    val isMe: Boolean,
    val timestamp: String
)

data class ChatUiState(
    val projectTitle: String = "Project 1",
    val activeOrganisationName: String = "Organisation 1",
    val searchQuery: String = "",
    val selectedConversationId: String? = null,
    val input: String = "",
    val conversations: List<ConversationUi> = emptyList(),
    val messages: List<ChatMessageUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)