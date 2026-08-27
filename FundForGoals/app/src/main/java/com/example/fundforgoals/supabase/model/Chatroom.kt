package com.example.fundforgoals.supabase.model


import kotlinx.serialization.Serializable

@Serializable
data class Chatroom (
    val id: Int? = null,
    val member1: Int,
    val member2: Int,
    val project: Int
)

@Serializable
data class CreateChatroomRequest (
    val member1: Int,
    val member2: Int,
    val project: Int
)

@Serializable
data class UpdateChatroomRequest (
    val member1: Int,
    val member2: Int
)