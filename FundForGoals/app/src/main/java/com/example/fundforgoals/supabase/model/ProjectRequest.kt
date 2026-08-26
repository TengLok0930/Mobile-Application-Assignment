package com.example.fundforgoals.supabase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectRequest(
    val id: Int? = null,

    @SerialName("created_at")
    val createdAt: String,

    val details: String,
    val aiOverview: String,
    val status: String,
    val projectId: Int
)

@Serializable
data class CreateProjectRequestRequest (
    val details: String,
    val aiOverview: String,
    val status: String,
    val projectId: Int
)

@Serializable
data class UpdateProjectRequestRequest(
    val details: String,
    val aiOverview: String,
    val status: String,
)