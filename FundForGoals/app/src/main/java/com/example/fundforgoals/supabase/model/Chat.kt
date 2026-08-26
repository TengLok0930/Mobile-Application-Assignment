package com.example.fundforgoals.supabase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: Int? = null,
    @SerialName("created_at")
    val createdAt: String,
    val content: String,
    val chatroom: Int,
    val sender: Int
)

@Serializable
data class CreateChatRequest(
    val content: String,
    val chatroom: Int,
    val sender: Int
)

@Serializable
data class UpdateChatRequest(
    val content: String,
    val chatroom: Int,
    val sender: Int
)