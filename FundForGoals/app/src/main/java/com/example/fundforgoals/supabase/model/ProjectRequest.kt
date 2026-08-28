package com.example.fundforgoals.supabase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestProject(
    val id: Int,
    val title: String
)
@Serializable
data class ProjectRequest(
    val id: Int? = null,

    @SerialName("created_at")
    val createdAt: String,

    val details: String,

    @SerialName("ai_overview")
    val aiOverview: String? = null,

    val status: String,

    @SerialName("project_id")
    val projectId: Int,

    val project: RequestProject? = null
)

@Serializable
data class CreateProjectRequestRequest (
    val details: String,

    @SerialName("ai_overview")
    val aiOverview: String? = null,
    val status: String,

    @SerialName("project_id")
    val projectId: Int
)

@Serializable
data class UpdateProjectRequestRequest(
    val details: String,

    @SerialName("ai_overview")
    val aiOverview: String? = null,

    val status: String,
)