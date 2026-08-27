package com.example.fundforgoals.feature.chat.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.Chat
import com.example.fundforgoals.supabase.model.Chatroom
import com.example.fundforgoals.supabase.repository.ChatRepository
import com.example.fundforgoals.supabase.repository.ChatroomRepository
import com.example.fundforgoals.supabase.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userRepository = UserRepository()
    private val chatRepository = ChatRepository()
    private val chatroomRepository = ChatroomRepository()

    private val currentUserName: String =
        checkNotNull(savedStateHandle["currentUser"])

    private val _uiState = MutableStateFlow(
        ChatUiState(
            currentUserName = currentUserName
        )
    )

    val uiState: StateFlow<ChatUiState> =
        _uiState.asStateFlow()

    init {
        initializeChat()
    }

    private fun initializeChat() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            runCatching {
                val user = userRepository
                    .getUserByUsername(currentUserName)
                    ?: error("User not found")

                val userId = user.id
                    ?: error("User ID is missing")

                val chatrooms = chatroomRepository
                    .getChatroomByUserId(userId)

                val firstChatroom = chatrooms.firstOrNull()

                val chats = firstChatroom?.id?.let { chatroomId ->
                    chatRepository.getChatsByChatroom(chatroomId)
                }.orEmpty()

                val otherMemberIds = chatrooms.mapNotNull { chatroom ->
                    if (chatroom.member1 == userId) chatroom.member2 else chatroom.member1
                }.distinct()

                val avatars = buildMap {
                    put(userId, user.avatarUrl)
                    otherMemberIds.forEach { memberId ->
                        userRepository.getUserById(memberId)?.let { member ->
                            put(memberId, member.avatarUrl)
                        }
                    }
                }

                InitialChatData(
                    userId = userId,
                    chatrooms = chatrooms,
                    selectedChatroom = firstChatroom,
                    chats = chats,
                    avatars = avatars
                )
            }.onSuccess { data ->
                _uiState.update {
                    it.copy(
                        currentUserId = data.userId,
                        chatrooms = data.chatrooms,
                        selectedChatroom = data.selectedChatroom,
                        chats = data.chats,
                        userAvatars = data.avatars,
                        project = data.selectedChatroom?.project,
                        isLoading = false
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to initialize chat"
                    )
                }
            }
        }
    }

    private fun selectChatroom(chatroom: Chatroom) {
        val chatroomId = chatroom.id ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                chatRepository.getChatsByChatroom(chatroomId)
            }.onSuccess { chats ->
                _uiState.update {
                    it.copy(
                        selectedChatroom = chatroom,
                        chats = chats,
                        project = chatroom.project,
                        isLoading = false
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message
                            ?: "Failed to load messages"
                    )
                }
            }
        }
    }

    private fun sendMessage() {
        val state = _uiState.value
        val content = state.chatInput.trim()
        val chatroomId = state.selectedChatroom?.id
        val userId = state.currentUserId

        if (
            content.isBlank() ||
            chatroomId == null ||
            userId == null ||
            state.isSending
        ) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSending = true,
                    errorMessage = null
                )
            }

            runCatching {
                chatRepository.addChat(
                    content = content,
                    chatroomId = chatroomId,
                    senderId = userId
                )

                chatRepository.getChatsByChatroom(chatroomId)
            }.onSuccess { updatedChats ->
                _uiState.update {
                    it.copy(
                        chats = updatedChats,
                        chatInput = "",
                        isSending = false
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isSending = false,
                        errorMessage = exception.message
                            ?: "Failed to send message"
                    )
                }
            }
        }
    }

    fun onAction(action: ChatAction) {
        when (action) {
            ChatAction.OnBackClick -> Unit
            ChatAction.OnSearchClick -> Unit
            ChatAction.OnAddClick -> Unit

            ChatAction.OnSendClick -> {
                sendMessage()
            }

            is ChatAction.OnInputChanged -> {
                _uiState.update {
                    it.copy(chatInput = action.value)
                }
            }

            is ChatAction.OnSearchQueryChanged -> {
                _uiState.update {
                    it.copy(searchQuery = action.value)
                }
            }

            is ChatAction.OnChatroomSelected -> {
                selectChatroom(action.chatroom)
            }
        }
    }

    private data class InitialChatData(
        val userId: Int,
        val chatrooms: List<Chatroom>,
        val selectedChatroom: Chatroom?,
        val chats: List<Chat>,
        val avatars: Map<Int, String>
    )
}