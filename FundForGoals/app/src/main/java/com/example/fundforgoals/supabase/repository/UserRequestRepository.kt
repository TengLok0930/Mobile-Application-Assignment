package com.example.fundforgoals.supabase.repository

import com.example.fundforgoals.supabase.model.CreateUserRequestRequest
import com.example.fundforgoals.supabase.model.UpdateUserRequestRequest
import com.example.fundforgoals.supabase.model.UserRequest
import com.example.fundforgoals.supabase.supabase
import io.github.jan.supabase.postgrest.from

class UserRequestRepository {

    suspend fun getUserRequests(): List<UserRequest> {
        return supabase
            .from("user_request")
            .select()
            .decodeList<UserRequest>()
    }

    suspend fun addUserRequest(userRequest: UserRequest) {
        val request = CreateUserRequestRequest(
            details = userRequest.details,
            aiOverview = userRequest.aiOverview,
            status = userRequest.status,
            userId = userRequest.userId
        )

        supabase
            .from("user_request")
            .insert(request)
    }

    suspend fun modifyUserRequest(userRequest: UserRequest) {
        val userRequestId = userRequest.id
            ?: throw IllegalArgumentException("User Request ID is required for updating")

        val request = UpdateUserRequestRequest(
            details = userRequest.details,
            aiOverview = userRequest.aiOverview,
            status = userRequest.status
        )

        supabase
            .from("user_request")
            .update(request) {
                filter {
                    eq("id", userRequestId)
                }
            }
    }

    suspend fun deleteUserRequest(id: Int) {
        supabase
            .from("user_request")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}