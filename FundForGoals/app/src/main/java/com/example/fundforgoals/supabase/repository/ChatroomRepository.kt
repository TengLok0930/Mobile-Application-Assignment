package com.example.fundforgoals.supabase.repository

import com.example.fundforgoals.supabase.model.Chatroom
import com.example.fundforgoals.supabase.model.CreateChatroomRequest
import com.example.fundforgoals.supabase.supabase
import io.github.jan.supabase.postgrest.from

class ChatroomRepository {

    private val contributorRepository = ContributorRepository()

    suspend fun getChatrooms(): List<Chatroom> {
        return supabase
            .from("chatroom")
            .select()
            .decodeList<Chatroom>()
    }

    suspend fun addChatroom(
        projectId: Int
    ) {
        val request = CreateChatroomRequest(
            project = projectId
        )

        supabase
            .from("chatroom")
            .insert(request)
    }

    suspend fun getChatroomsByUserId(
        userId: Int
    ): List<Chatroom> {
        val contributors =
            contributorRepository.getContributorsByUserId(userId)

        val projectIds = contributors.map { it.project }

        if (projectIds.isEmpty()) {
            return emptyList()
        }

        val allChatrooms = getChatrooms()

        return allChatrooms.filter { chatroom ->
            chatroom.project in projectIds
        }
    }

    suspend fun getChatroomById(
        id: Int
    ): Chatroom? {
        return supabase
            .from("chatroom")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeList<Chatroom>()
            .firstOrNull()
    }
}