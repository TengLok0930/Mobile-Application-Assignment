package com.example.fundforgoals.supabase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: Int? = null,

    @SerialName("created_at")
    val createdAt: String,

    val title: String,
    val desc: String,

    @SerialName("created_by")
    val createdBy: Int,

    @SerialName("fund_goal")
    val fundGoal: Double,

    @SerialName("current_fund")
    val currentFund: Double,

    @SerialName("avatar_url")
    val avatarUrl: String
)

@Serializable
data class CreateProjectRequest(
    val title: String,
    val desc: String,

    @SerialName("created_by")
    val createdBy: Int,

    @SerialName("fund_goal")
    val fundGoal: Double,

    @SerialName("current_fund")
    val currentFund: Double,

    @SerialName("avatar_url")
    val avatarUrl: String
)

@Serializable
data class UpdateProjectRequest(
    val title: String,
    val desc: String,

    @SerialName("fund_goal")
    val fundGoal: Double,

    @SerialName("current_fund")
    val currentFund: Double,

    @SerialName("avatar_url")
    val avatarUrl: String
)