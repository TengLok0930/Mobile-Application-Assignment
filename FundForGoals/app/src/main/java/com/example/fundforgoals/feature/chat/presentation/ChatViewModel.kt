package com.example.fundforgoals.feature.chat.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ChatUiState(
            projectTitle = "Project 1",
            messages = listOf(
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
            )
        )
    )
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onAction(action: ChatAction) {
        when (action) {
            is ChatAction.OnInputChanged -> {
                _uiState.update { it.copy(input = action.value) }
            }

            ChatAction.OnSendClick -> {
                val currentInput = _uiState.value.input.trim()
                if (currentInput.isBlank()) return

                val newMessage = ChatMessageUi(
                    id = System.currentTimeMillis().toString(),
                    text = currentInput,
                    isMe = true,
                    timestamp = "19/7/2026"
                )

                _uiState.update {
                    it.copy(
                        input = "",
                        messages = it.messages + newMessage
                    )
                }
            }

            ChatAction.OnBackClick,
            ChatAction.OnSearchClick,
            ChatAction.OnAddClick -> Unit
        }
    }
}