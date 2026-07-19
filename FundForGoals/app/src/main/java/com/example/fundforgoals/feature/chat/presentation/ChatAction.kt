package com.example.fundforgoals.feature.chat.presentation

sealed interface ChatAction {
    data class OnInputChanged(val value: String) : ChatAction
    data object OnSendClick : ChatAction
    data object OnBackClick : ChatAction
    data object OnSearchClick : ChatAction
    data object OnAddClick : ChatAction
}