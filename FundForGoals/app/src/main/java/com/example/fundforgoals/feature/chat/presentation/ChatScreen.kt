package com.example.fundforgoals.feature.chat.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fundforgoals.R
import com.example.fundforgoals.app.navigation.AppBottomBar
import com.example.fundforgoals.app.navigation.AppNavigationRail
import com.example.fundforgoals.core.ui.theme.BrandAccentDark
import com.example.fundforgoals.core.ui.theme.BrandAccentLight
import com.example.fundforgoals.core.ui.theme.FundForGoalsTheme
import com.example.fundforgoals.supabase.model.Chat
import com.example.fundforgoals.supabase.model.Chatroom
import com.example.fundforgoals.supabase.model.Project

@Composable
fun ChatScreen(
    uiState: ChatUiState,
    onAction: (ChatAction) -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true
) {
    if (isCompact) {
        ChatCompactScreen(
            uiState = uiState,
            onAction = onAction,
            onHomeClick = onHomeClick,
            onProfileClick = onProfileClick,
            modifier = modifier
        )
    } else {
        ChatExpandedScreen(
            uiState = uiState,
            onAction = onAction,
            onHomeClick = onHomeClick,
            onProfileClick = onProfileClick,
            modifier = modifier
        )
    }
}

@Composable
private fun ChatCompactScreen(
    uiState: ChatUiState,
    onAction: (ChatAction) -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showChatDetail by rememberSaveable { mutableStateOf(false) }

    if (!showChatDetail) {
        ChatroomListScreen(
            uiState = uiState,
            onSearchChanged = { query ->
                onAction(ChatAction.OnSearchQueryChanged(query))
            },
            onChatroomClick = { chatroom ->
                onAction(ChatAction.OnChatroomSelected(chatroom))
                showChatDetail = true
            },
            onHomeClick = onHomeClick,
            onProfileClick = onProfileClick,
            modifier = modifier
        )
    } else {
        ChatDetailScreen(
            uiState = uiState,
            onAction = onAction,
            onBackClick = { showChatDetail = false },
            modifier = modifier
        )
    }
}

@Composable
private fun ChatroomListScreen(
    uiState: ChatUiState,
    onSearchChanged: (String) -> Unit,
    onChatroomClick: (Chatroom) -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomBar(
                selectedItem = "messages",
                onMessagesClick = {},
                onHomeClick = onHomeClick,
                onProfileClick = onProfileClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                uiState.errorMessage?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }

                Text(
                    modifier = Modifier.weight(1f),
                    text = "Messages",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(48.dp))
            }

            ChatroomPane(
                searchText = uiState.searchQuery,
                selectedChatroom = uiState.selectedChatroom,
                chatrooms = uiState.filteredChatrooms,
                projectsById = uiState.projectsById,
                onSearchChanged = onSearchChanged,
                onChatroomSelected = onChatroomClick
            )
        }
    }
}

@Composable
private fun ChatDetailScreen(
    uiState: ChatUiState,
    onAction: (ChatAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = if (isSystemInDarkTheme()) {
        BrandAccentDark
    } else {
        BrandAccentLight
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Chatroom",
                        color = accentColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = uiState.selectedProjectName,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )

            ChatConversationContent(
                uiState = uiState,
                onAction = onAction,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun ChatExpandedScreen(
    uiState: ChatUiState,
    onAction: (ChatAction) -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = if (isSystemInDarkTheme()) {
        BrandAccentDark
    } else {
        BrandAccentLight
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(20.dp)
        ) {
            AppNavigationRail(
                selectedItem = "messages",
                onMessagesClick = {},
                onHomeClick = onHomeClick,
                onProfileClick = onProfileClick
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Chatroom",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(modifier = Modifier.fillMaxSize()) {
                    Card(
                        modifier = Modifier
                            .weight(0.95f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        ChatroomPane(
                            searchText = uiState.searchQuery,
                            selectedChatroom = uiState.selectedChatroom,
                            chatrooms = uiState.filteredChatrooms,
                            projectsById = uiState.projectsById,
                            onSearchChanged = { query ->
                                onAction(ChatAction.OnSearchQueryChanged(query))
                            },
                            onChatroomSelected = { chatroom ->
                                onAction(ChatAction.OnChatroomSelected(chatroom))
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Card(
                        modifier = Modifier
                            .weight(1.65f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 16.dp)
                            ) {
                                Text(
                                    text = "Chatroom",
                                    color = accentColor,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = uiState.selectedProjectName,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            )

                            ChatConversationContent(
                                uiState = uiState,
                                onAction = onAction,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatConversationContent(
    uiState: ChatUiState,
    onAction: (ChatAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.selectedChatroom, uiState.chats.size) {
        if (uiState.chats.isNotEmpty()) {
            listState.animateScrollToItem(uiState.chats.lastIndex)
        }
    }

    Column(modifier = modifier) {
        if (uiState.chats.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No messages yet.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = uiState.chats,
                    key = { chat -> chat.id ?: "${chat.chatroom}-${chat.createdAt}" }
                ) { chat ->
                    ChatBubble(
                        chat = chat,
                        currentUserId = uiState.currentUserId,
                        avatarUrl = uiState.userAvatars[chat.sender]
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.chatInput,
                onValueChange = { onAction(ChatAction.OnInputChanged(it)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = { Text("Type a message") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                singleLine = true
            )

            IconButton(
                onClick = { onAction(ChatAction.OnSendClick) },
                enabled = uiState.chatInput.isNotBlank() &&
                        uiState.selectedChatroom != null &&
                        uiState.currentUserId != null &&
                        !uiState.isSending,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.send_24px),
                    contentDescription = "Send"
                )
            }
        }
    }
}

@Composable
private fun ChatroomPane(
    searchText: String,
    selectedChatroom: Chatroom?,
    chatrooms: List<Chatroom>,
    projectsById: Map<Int, Project>,
    onSearchChanged: (String) -> Unit,
    onChatroomSelected: (Chatroom) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchChanged,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = { Text("Search chatroom") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search_24px),
                    contentDescription = "Search"
                )
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (chatrooms.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No chatrooms found.")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = chatrooms,
                    key = { chatroom ->
                        chatroom.id ?: "project-${chatroom.project}"
                    }
                ) { chatroom ->
                    ChatroomItem(
                        chatroom = chatroom,
                        project = projectsById[chatroom.project],
                        isSelected = chatroom.id == selectedChatroom?.id,
                        onClick = { onChatroomSelected(chatroom) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatroomItem(
    chatroom: Chatroom,
    project: Project?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            val avatarUrl = project?.avatarUrl
            if (!avatarUrl.isNullOrBlank()) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = project?.title ?: "Project ${chatroom.project}",
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ChatBubble(
    chat: Chat,
    currentUserId: Int?,
    avatarUrl: String?
) {
    val isMe = currentUserId != null && chat.sender == currentUserId

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMe) {
            ChatAvatar(avatarUrl)
            Spacer(modifier = Modifier.width(6.dp))
        }

        ChatBox(
            content = chat.content,
            timestamp = chat.createdAt,
            isMe = isMe
        )

        if (isMe) {
            Spacer(modifier = Modifier.width(6.dp))
            ChatAvatar(avatarUrl)
        }
    }
}

@Composable
private fun ChatAvatar(avatarUrl: String?) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        if (!avatarUrl.isNullOrBlank()) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun ChatBox(
    content: String,
    timestamp: String,
    isMe: Boolean
) {
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = if (isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
            .widthIn(max = 250.dp)
    ) {
        Column {
            Text(text = content, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = timestamp,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(name = "Compact Light", showBackground = true)
@Preview(
    name = "Compact Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun ChatCompactPreview() {
    FundForGoalsTheme {
        ChatScreen(
            uiState = ChatUiState(),
            onAction = {},
            onHomeClick = {},
            onProfileClick = {},
            isCompact = true
        )
    }
}

@Preview(name = "Expanded Light", widthDp = 900, heightDp = 700, showBackground = true)
@Preview(
    name = "Expanded Dark",
    widthDp = 900,
    heightDp = 700,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun ChatExpandedPreview() {
    FundForGoalsTheme {
        ChatScreen(
            uiState = ChatUiState(),
            onAction = {},
            onHomeClick = {},
            onProfileClick = {},
            isCompact = false
        )
    }
}