package com.example.fundforgoals.feature.chat.presentation

data class ChatMessageUi(
    val id: String,
    val text: String,
    val isMe: Boolean,
    val timestamp: String
)

data class ChatUiState(
    val projectTitle: String = "Project 1",
    val input: String = "",
    val messages: List<ChatMessageUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)