package com.example.fundforgoals.feature.chat.presentation

import com.example.fundforgoals.supabase.model.Chatroom

sealed interface ChatAction {
    data object OnBackClick : ChatAction
    data object OnSearchClick : ChatAction
    data object OnAddClick : ChatAction
    data object OnSendClick : ChatAction

    data class OnInputChanged(
        val value: String
    ) : ChatAction

    data class OnSearchQueryChanged(
        val value: String
    ) : ChatAction

    data class OnChatroomSelected(
        val chatroom: Chatroom
    ) : ChatAction
}