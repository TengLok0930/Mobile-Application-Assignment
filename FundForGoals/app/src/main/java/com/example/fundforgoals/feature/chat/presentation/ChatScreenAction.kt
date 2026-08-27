package com.example.fundforgoals.feature.chat.presentation

sealed interface ChatScreenAction {
    data class OnInputChanged(val value: String) : ChatScreenAction
    data class OnSearchQueryChanged(val value: String) : ChatScreenAction
    data class OnConversationSelected(val conversationId: String) : ChatScreenAction

    data object OnSendClick : ChatScreenAction
    data object OnBackClick : ChatScreenAction
    data object OnSearchClick : ChatScreenAction
    data object OnAddClick : ChatScreenAction
}