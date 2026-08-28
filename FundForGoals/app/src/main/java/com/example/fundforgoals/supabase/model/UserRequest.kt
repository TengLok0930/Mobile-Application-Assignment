package com.example.fundforgoals.supabase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestUser(
    val id: Int,
    val name: String,

    @SerialName("user_type")
    val userType: String? = null
)
@Serializable
data class UserRequest(
    val id: Int? = null,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("user_id")
    val userId: Int,

    @SerialName("request_type")
    val requestType: String,

    val details: String,

    @SerialName("ai_overview")
    val aiOverview: String? = null,

    val status: String,
    val user: RequestUser? = null
)

@Serializable
data class CreateUserRequestRequest (
    @SerialName("user_id")
    val userId: Int,

    @SerialName("request_type")
    val requestType: String,

    val details: String,

    @SerialName("ai_overview")
    val aiOverview: String? = null,

    val status: String,
)

@Serializable
data class UpdateUserRequestRequest(
    @SerialName("request_type")
    val requestType: String,

    val details: String,

    @SerialName("ai_overview")
    val aiOverview: String? = null,

    val status: String
)