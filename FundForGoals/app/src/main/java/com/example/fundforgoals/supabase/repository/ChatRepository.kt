package com.example.fundforgoals.supabase.repository


import com.example.fundforgoals.supabase.model.Chat
import com.example.fundforgoals.supabase.model.CreateChatRequest
import com.example.fundforgoals.supabase.model.UpdateChatRequest
import com.example.fundforgoals.supabase.supabase
import io.github.jan.supabase.postgrest.from


class ChatRepository {

    suspend fun getChat(): List<Chat> {
        return supabase
            .from("chat")
            .select()
            .decodeList<Chat>()
    }

    suspend fun addChat(
        content: String,
        chatroomId: Int,
        senderId: Int
    ) {
        val request = CreateChatRequest(
            content = content,
            chatroom = chatroomId,
            sender = senderId
        )

        supabase
            .from("chat")
            .insert(request)
    }

    suspend fun modifyChat(chat: Chat) {
        val chatId = chat.id
            ?: throw IllegalArgumentException("Chat ID is required for updating")

        val request = UpdateChatRequest(
            content = chat.content,
            chatroom = chat.chatroom,
            sender = chat.sender
        )

        supabase
            .from("chat")
            .update(request) {
                filter {
                    eq("id", chatId)
                }
            }
    }

    suspend fun deleteChat(id: Int) {
        supabase
            .from("chat")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }

    suspend fun getChatsByChatroom(
        chatroomId: Int
    ): List<Chat> {
        return supabase
            .from("chat")
            .select {
                filter {
                    eq("chatroom", chatroomId)
                }
            }
            .decodeList<Chat>()
    }
}
