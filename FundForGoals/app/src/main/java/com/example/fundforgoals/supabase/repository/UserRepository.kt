package com.example.fundforgoals.supabase.repository

import com.example.fundforgoals.supabase.model.User
import com.example.fundforgoals.supabase.supabase
import io.github.jan.supabase.postgrest.from

class UserRepository {

    suspend fun getUsers(): List<User> {
        return supabase
            .from("user")
            .select()
            .decodeList<User>()
    }

    suspend fun addUser(
        id: Int,
        name: String,
        socialLink: String,
        avatarUrl: String,
        userType: String
    ) {
        supabase
            .from("user")
            .insert(
                mapOf(
                    "id" to id,
                    "name" to name,
                    "social_link" to socialLink,
                    "avatar_url" to avatarUrl,
                    "user_type" to userType
                )
            )
    }

    suspend fun deleteUser(id: Int) {
        supabase
            .from("user")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}