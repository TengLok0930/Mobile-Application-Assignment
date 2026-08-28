package com.example.fundforgoals.supabase.model


import kotlinx.serialization.Serializable

@Serializable
data class Chatroom (
    val id: Int? = null,
    val project: Int
)

@Serializable
data class CreateChatroomRequest (
    val project: Int
)