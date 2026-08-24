package com.example.fundforgoals.supabase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val password: String,

    @SerialName("social_link")
    val socialLink: String,

    @SerialName("avatar_url")
    val avatarUrl: String,

    @SerialName("user_type")
    val userType: String
)
