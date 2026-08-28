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

    suspend fun addUser(user: User): User {
        val request = CreateUserRequest(
            name = user.name,
            password = user.password,
            socialLink = user.socialLink,
            avatarUrl = user.avatarUrl,
            userType = user.userType,
            isApproved = user.isApproved
        )

        return supabase
            .from("user")
            .insert(request) {
                select()
            }
            .decodeSingle<User>()
    }

    suspend fun modifyUser(user: User): User {
        val userId = user.id
            ?: throw IllegalArgumentException("User ID is required for updating")

        val request = UpdateUserRequest(
            name = user.name,
            password = user.password,
            socialLink = user.socialLink,
            avatarUrl = user.avatarUrl,
            userType = user.userType,
            isApproved = user.isApproved
        )

        return supabase
            .from("user")
            .update(request) {
                filter {
                    eq("id", userId)
                }
                select()
            }
            .decodeSingle<User>()
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

    suspend fun getUserById(id: Int): User? {
        return supabase
            .from("user")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeList<User>()
            .firstOrNull()
    }

    suspend fun updateUserApproval(
        id: Int,
        isApproved: Boolean
    ): User {
        return supabase
            .from("user")
            .update(
                mapOf("is_approved" to isApproved)
            ) {
                filter {
                    eq("id", id)
                }
                select()
            }
            .decodeSingle<User>()
    }

    suspend fun updateUserPassword(
        id: Int,
        newPassword: String
    ): User {
        return supabase
            .from("user")
            .update(
                mapOf("password" to newPassword)
            ) {
                filter {
                    eq("id", id)
                }
                select()
            }
            .decodeSingle<User>()
    }
}