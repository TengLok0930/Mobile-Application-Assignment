package com.example.fundforgoals.supabase.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.Chat
import com.example.fundforgoals.supabase.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatUiState(
    val isLoading: Boolean = false,
    val chats: List<Chat> = emptyList(),
    val error: String? = null
)

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadChats()
    }

    fun loadChats() {
        viewModelScope.launch {
            executeRequest{
                repository.getChat()
            }?.let { chats ->
                _uiState.value = ChatUiState(
                    isLoading = false,
                    chats = chats
                )
            }
        }
    }

    fun addChat(chat: Chat) {
        viewModelScope.launch {
            executeAction{
                repository.addChat(chat)
            }
        }
    }

    fun modifyChat(chat: Chat) {
        viewModelScope.launch {
            executeAction {
                repository.modifyChat(chat)
            }
        }
    }

    fun deleteChat(id: Int) {
        viewModelScope.launch {
            executeAction {
                repository.deleteChat(id)
            }
        }
    }

    private suspend fun executeAction(
        action: suspend () -> Unit
    ) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        try {
            action()

            val chats = repository.getChat()

            _uiState.value = ChatUiState(
                isLoading = false,
                chats = chats
            )
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = exception.message ?: "Operation failed"
            )
        }
    }

    private suspend fun executeRequest(
        request: suspend () -> List<Chat>
    ): List<Chat>? {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        return try {
            request()
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = exception.message ?: "Unable to load chats"
            )
            null
        }
    }
}