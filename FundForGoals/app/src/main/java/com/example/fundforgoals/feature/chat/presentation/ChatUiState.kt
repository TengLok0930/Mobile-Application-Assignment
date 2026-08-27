package com.example.fundforgoals.feature.chat.presentation

import com.example.fundforgoals.supabase.model.Chat
import com.example.fundforgoals.supabase.model.Chatroom

data class ChatUiState(
    val currentUserName: String = "",
    val currentUserId: Int? = null,

    val chatrooms: List<Chatroom> = emptyList(),
    val selectedChatroom: Chatroom? = null,
    val chats: List<Chat> = emptyList(),
    val userAvatars: Map<Int, String> = emptyMap(),

    val searchQuery: String = "",
    val project: Int? = null,
    val chatInput: String = "",

    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val errorMessage: String? = null
) {
    val filteredChatrooms: List<Chatroom>
        get() {
            if (searchQuery.isBlank()) {
                return chatrooms
            }

            return chatrooms.filter { chatroom ->
                chatroom.project
                    .toString()
                    .contains(searchQuery, ignoreCase = true)
            }
        }
}