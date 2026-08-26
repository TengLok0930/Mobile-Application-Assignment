package com.example.fundforgoals.supabase.repository

import com.example.fundforgoals.supabase.model.User
import com.example.fundforgoals.supabase.model.CreateUserRequest
import com.example.fundforgoals.supabase.model.UpdateUserRequest
import com.example.fundforgoals.supabase.supabase
import io.github.jan.supabase.postgrest.from

class UserRepository {

    suspend fun getUsers(): List<User> {
        return supabase
            .from("user")
            .select()
            .decodeList<User>()
    }

    suspend fun addUser(user: User) {
        val request = CreateUserRequest(
            name = user.name,
            password = user.password,
            socialLink = user.socialLink,
            avatarUrl = user.avatarUrl,
            userType = user.userType
        )

        supabase
            .from("user")
            .insert(request)
    }

    suspend fun modifyUser(user: User) {
        val userId = user.id
            ?: throw IllegalArgumentException("User ID is required for updating")

        val request = UpdateUserRequest(
            name = user.name,
            password = user.password,
            socialLink = user.socialLink,
            avatarUrl = user.avatarUrl,
            userType = user.userType
        )

        supabase
            .from("user")
            .update(request) {
                filter {
                    eq("id", userId)
                }
            }
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

    suspend fun getUserByUsername(username: String): User? {
        return supabase
            .from("user")
            .select {
                filter {
                    eq("name", username)
                }
            }
            .decodeList<User>()
            .firstOrNull()
    }
}