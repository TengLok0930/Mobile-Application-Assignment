package com.example.fundforgoals.supabase.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.Chatroom
import com.example.fundforgoals.supabase.repository.ChatroomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class ChatroomUiState(
    val isLoading: Boolean = false,
    val chatroom: List<Chatroom> = emptyList(),
    val error: String? = null
)

class ChatroomViewModel : ViewModel() {
    private val repository = ChatroomRepository()

    private val _uiState = MutableStateFlow(ChatroomUiState())
    val uiState: StateFlow<ChatroomUiState> = _uiState.asStateFlow()

    init {
        loadChatroom()
    }

    fun loadChatroom() {
        viewModelScope.launch {
            executeRequest {
                repository.getChatroom()
            }?.let { chatroom ->
                _uiState.value = ChatroomUiState(
                    isLoading = false,
                    chatroom = chatroom
                )
            }
        }
    }

    fun addChatroom(chatroom: Chatroom) {
        viewModelScope.launch {
            executeAction {
                repository.addChatroom(chatroom)
            }
        }
    }

    fun modifyChatroom(chatroom: Chatroom) {
        viewModelScope.launch {
            executeAction {
                repository.modifyChatroom(chatroom)
            }
        }
    }

    fun deleteChatroom(id: Int) {
        viewModelScope.launch {
            executeAction {
                repository.deleteChatroom(id)
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

            val chatroom = repository.getChatroom()

            _uiState.value = ChatroomUiState(
                isLoading = false,
                chatroom = chatroom
            )
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = exception.message ?: "Operation failed"
            )
        }
    }

    private suspend fun executeRequest(
        request: suspend () -> List<Chatroom>
    ): List<Chatroom>? {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        return try {
            request()
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = exception.message ?: "Unable to load chatroom"
            )
            null
        }
    }
}