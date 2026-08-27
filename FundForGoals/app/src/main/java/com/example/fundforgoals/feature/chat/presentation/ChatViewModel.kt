package com.example.fundforgoals.feature.chat.presentation

import androidx.lifecycle.ViewModel
import com.example.fundforgoals.supabase.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatViewModel : ViewModel() {
    val chatRepository = ChatRepository()

    private val allConversations = listOf(
        ConversationUi(
            id = "1",
            title = "Project 1",
            subtitle = "Fundraising discussion",
            date = "19/7/2026"
        ),
        ConversationUi(
            id = "2",
            title = "Project 2",
            subtitle = "Donation follow-up",
            date = "18/7/2026"
        )
    )

    private val conversationMessages = mutableMapOf(
        "1" to listOf(
            ChatMessageUi(
                id = "1",
                text = "Hi, I want to fund this project",
                isMe = true,
                timestamp = "19/7/2026"
            ),
            ChatMessageUi(
                id = "2",
                text = "Hi, how much do you want to fund?",
                isMe = false,
                timestamp = "19/7/2026"
            )
        ),
        "2" to listOf(
            ChatMessageUi(
                id = "3",
                text = "Hello, is this project still accepting support?",
                isMe = true,
                timestamp = "18/7/2026"
            ),
            ChatMessageUi(
                id = "4",
                text = "Yes, contributions are still open.",
                isMe = false,
                timestamp = "18/7/2026"
            )
        )
    )

    private val _uiState = MutableStateFlow(
        ChatUiState(
            projectTitle = "Project 1",
            activeOrganisationName = "Organisation 1",
            selectedConversationId = "1",
            conversations = allConversations,
            messages = conversationMessages["1"].orEmpty()
        )
    )
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onAction(action: ChatAction) {
        when (action) {
            is ChatAction.OnInputChanged -> {
                _uiState.update { it.copy(input = action.value) }
            }

            is ChatAction.OnSearchQueryChanged -> {
                val filtered = if (action.value.isBlank()) {
                    allConversations
                } else {
                    allConversations.filter {
                        it.title.contains(action.value, ignoreCase = true) ||
                                it.subtitle.contains(action.value, ignoreCase = true)
                    }
                }

                _uiState.update {
                    it.copy(
                        searchQuery = action.value,
                        conversations = filtered
                    )
                }
            }

            is ChatAction.OnConversationSelected -> {
                val selectedConversation = allConversations.firstOrNull {
                    it.id == action.conversationId
                }

                _uiState.update {
                    it.copy(
                        selectedConversationId = action.conversationId,
                        projectTitle = selectedConversation?.title ?: it.projectTitle,
                        activeOrganisationName = selectedConversation?.subtitle ?: it.activeOrganisationName,
                        messages = conversationMessages[action.conversationId].orEmpty(),
                        input = ""
                    )
                }
            }

            ChatAction.OnSendClick -> {
                val currentState = _uiState.value
                val currentInput = currentState.input.trim()
                val conversationId = currentState.selectedConversationId ?: return

                if (currentInput.isBlank()) return

                val newMessage = ChatMessageUi(
                    id = System.currentTimeMillis().toString(),
                    text = currentInput,
                    isMe = true,
                    timestamp = "23/7/2026"
                )

                val updatedMessages = conversationMessages[conversationId].orEmpty() + newMessage
                conversationMessages[conversationId] = updatedMessages

                _uiState.update {
                    it.copy(
                        input = "",
                        messages = updatedMessages
                    )
                }
            }

            ChatAction.OnBackClick,
            ChatAction.OnSearchClick,
            ChatAction.OnAddClick -> Unit
        }
    }
}