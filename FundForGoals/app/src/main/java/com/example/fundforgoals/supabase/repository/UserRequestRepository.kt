package com.example.fundforgoals.supabase.repository

import com.example.fundforgoals.supabase.model.CreateUserRequestRequest
import com.example.fundforgoals.supabase.model.UpdateUserRequestRequest
import com.example.fundforgoals.supabase.model.UserRequest
import com.example.fundforgoals.supabase.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class UserRequestRepository {

    suspend fun getUserRequests(): List<UserRequest> {
        return supabase
            .from("user_request")
            .select(
                Columns.raw(
                    """
                    id,
                    created_at,
                    user_id,
                    request_type,
                    details,
                    ai_overview,
                    status,
                    user:"user" (
                        id,
                        name,
                        user_type
                    )
                    """.trimIndent()
                )
            ) {
                filter {
                    eq("status", "pending")
                }
            }
            .decodeList<UserRequest>()
    }

    suspend fun addUserRequest(userRequest: UserRequest) {
        val request = CreateUserRequestRequest(
            userId = userRequest.userId,
            requestType = userRequest.requestType,
            details = userRequest.details,
            aiOverview = userRequest.aiOverview,
            status = userRequest.status
        )

        supabase
            .from("user_request")
            .insert(request)
    }

    suspend fun modifyUserRequest(userRequest: UserRequest): UserRequest {
        val userRequestId = userRequest.id
            ?: throw IllegalArgumentException("User Request ID is required for updating")

        val request = UpdateUserRequestRequest(
            requestType = userRequest.requestType,
            details = userRequest.details,
            aiOverview = userRequest.aiOverview,
            status = userRequest.status
        )

        return supabase
            .from("user_request")
            .update(request) {
                filter {
                    eq("id", userRequestId)
                }
                select()
            }
            .decodeSingle<UserRequest>()
    }

    suspend fun updateUserRequestStatus(
        id: Int,
        status: String
    ) {
        supabase
            .from("user_request")
            .update(
                mapOf("status" to status)
            ) {
                filter {
                    eq("id", id)
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