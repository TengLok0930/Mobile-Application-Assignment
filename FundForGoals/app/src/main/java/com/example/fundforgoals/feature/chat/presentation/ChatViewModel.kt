package com.example.fundforgoals.feature.chat.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.Chat
import com.example.fundforgoals.supabase.model.Chatroom
import com.example.fundforgoals.supabase.model.Project
import com.example.fundforgoals.supabase.repository.ChatRepository
import com.example.fundforgoals.supabase.repository.ChatroomRepository
import com.example.fundforgoals.supabase.repository.ProjectRepository
import com.example.fundforgoals.supabase.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatUiState(
    val currentUserName: String = "",
    val currentUserId: Int? = null,

    val chatrooms: List<Chatroom> = emptyList(),
    val selectedChatroom: Chatroom? = null,
    val chats: List<Chat> = emptyList(),
    val userAvatars: Map<Int, String> = emptyMap(),
    val projectsById: Map<Int, Project> = emptyMap(),

    val searchQuery: String = "",
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
                val projectTitle = projectsById[chatroom.project]?.title.orEmpty()
                projectTitle.contains(searchQuery, ignoreCase = true)
            }
        }

    val selectedProjectName: String
        get() = selectedChatroom?.project
            ?.let { projectsById[it]?.title }
            .orEmpty()
}

class ChatViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userRepository = UserRepository()
    private val chatRepository = ChatRepository()
    private val chatroomRepository = ChatroomRepository()
    private val projectRepository = ProjectRepository()

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
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                val user = userRepository.getUserByUsername(currentUserName)
                    ?: error("User not found")
                val userId = user.id ?: error("User ID is missing")

                val chatrooms = chatroomRepository.getChatroomsByUserId(userId)
                val firstChatroom = chatrooms.firstOrNull()

                val chats = firstChatroom?.id?.let { chatroomId ->
                    chatRepository.getChatsByChatroom(chatroomId)
                }.orEmpty()

                val projectIds = chatrooms.map { it.project }.distinct()

                val projects = buildMap {
                    projectIds.forEach { projectId ->
                        projectRepository.getProjectById(projectId)?.let { put(projectId, it) }
                    }
                }

                // Fetch avatars for everyone who appears in the loaded chats, plus yourself
                val senderIds = (chats.map { it.sender } + userId).distinct()
                val avatars = buildMap {
                    senderIds.forEach { senderId ->
                        val avatarUrl = if (senderId == userId) {
                            user.avatarUrl
                        } else {
                            userRepository.getUserById(senderId)?.avatarUrl
                        }
                        if (avatarUrl != null) put(senderId, avatarUrl)
                    }
                }

                InitialChatData(
                    userId = userId,
                    chatrooms = chatrooms,
                    selectedChatroom = firstChatroom,
                    chats = chats,
                    avatars = avatars,
                    projects = projects
                )
            }.onSuccess { data ->
                _uiState.update {
                    it.copy(
                        currentUserId = data.userId,
                        chatrooms = data.chatrooms,
                        selectedChatroom = data.selectedChatroom,
                        chats = data.chats,
                        userAvatars = data.avatars,
                        projectsById = data.projects,
                        isLoading = false
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = exception.message ?: "Failed to initialize chat")
                }
            }
        }
    }

    private fun selectChatroom(chatroom: Chatroom) {
        val chatroomId = chatroom.id ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                val chats = chatRepository.getChatsByChatroom(chatroomId)

                // Merge in avatars for any senders we haven't fetched yet
                val existingAvatars = _uiState.value.userAvatars
                val newSenderIds = chats.map { it.sender }.distinct()
                    .filterNot { existingAvatars.containsKey(it) }

                val newAvatars = buildMap {
                    newSenderIds.forEach { senderId ->
                        userRepository.getUserById(senderId)?.avatarUrl?.let { put(senderId, it) }
                    }
                }

                chats to (existingAvatars + newAvatars)
            }.onSuccess { (chats, avatars) ->
                _uiState.update {
                    it.copy(
                        selectedChatroom = chatroom,
                        chats = chats,
                        userAvatars = avatars,
                        isLoading = false
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = exception.message ?: "Failed to load messages")
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
        val avatars: Map<Int, String>,
        val projects: Map<Int, Project>
    )
}