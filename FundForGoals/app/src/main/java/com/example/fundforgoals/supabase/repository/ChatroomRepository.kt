package com.example.fundforgoals.supabase.repository

import com.example.fundforgoals.supabase.model.Chatroom
import com.example.fundforgoals.supabase.model.CreateChatroomRequest
import com.example.fundforgoals.supabase.model.UpdateChatroomRequest
import com.example.fundforgoals.supabase.supabase
import io.github.jan.supabase.postgrest.from


class ChatroomRepository {

    suspend fun getChatroom(): List<Chatroom> {
        return supabase
            .from("chatroom")
            .select()
            .decodeList<Chatroom>()
    }

    suspend fun addChatroom(chatroom: Chatroom) {
        val request = CreateChatroomRequest(
            member1 = chatroom.member1,
            member2 = chatroom.member2,
            project = chatroom.project
        )

        supabase
            .from("chatroom")
            .insert(request)
    }

    suspend fun modifyChatroom(chatroom: Chatroom) {
        val chatroomId = chatroom.id
            ?: throw IllegalArgumentException("Chatroom ID is required for updating")


        val request = UpdateChatroomRequest(
            member1 = chatroom.member1,
            member2 = chatroom.member2
        )

        supabase
            .from("chatroom")
            .update(request) {
                filter {
                    eq("id", chatroomId)
                }
            }
    }

    suspend fun deleteChatroom(id: Int) {
        supabase
            .from("chatroom")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}