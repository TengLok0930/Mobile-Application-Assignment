package com.example.fundforgoals.feature.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.repository.ChatRepository
import com.example.fundforgoals.supabase.repository.ChatroomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatScreenViewModel : ViewModel() {
    val chatRepository = ChatRepository()
    val chatroomRepository = ChatroomRepository()

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onAction(action: ChatScreenAction) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    chatroom = chatroomRepository.getChatroom()
                )
            }
        }

        /*when (action) {
            is ChatScreenAction.OnInputChanged -> {
                _uiState.update { it.copy(input = action.value) }
            }

            is ChatScreenAction.OnSearchQueryChanged -> {
                val filtered = if (action.value.isBlank()) {
                    getChatroom()
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

            is ChatScreenAction.OnConversationSelected -> {
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

            ChatScreenAction.OnSendClick -> {
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

            ChatScreenAction.OnBackClick,
            ChatScreenAction.OnSearchClick,
            ChatScreenAction.OnAddClick -> Unit
        }*/
    }

    private fun getChatroom() {
        val currentState = _uiState.value


    }
}

