package com.example.fundforgoals.supabase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val id: Int? = null,

    @SerialName("created_at")
    val createdAt: String,

    val details: String,
    val aiOverview: String,
    val status: String,
    val userId: Int
)

@Serializable
data class CreateUserRequestRequest (
    val details: String,
    val aiOverview: String,
    val status: String,
    val userId: Int
)

@Serializable
data class UpdateUserRequestRequest(
    val details: String,
    val aiOverview: String,
    val status: String,
)