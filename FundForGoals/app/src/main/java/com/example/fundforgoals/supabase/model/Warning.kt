package com.example.fundforgoals.supabase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Warning(
    val id: Int? = null,

    @SerialName("created_at")
    val createdAt: String,

    val details: String,

    @SerialName("project_id")
    val projectId: Int
)

@Serializable
data class CreateWarningRequest(
    val details: String,

    @SerialName("project_id")
    val projectId: Int
)

@Serializable
data class UpdateWarningRequest(
    val details: String
)