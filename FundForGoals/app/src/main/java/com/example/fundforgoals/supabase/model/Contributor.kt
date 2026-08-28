package com.example.fundforgoals.supabase.model

import kotlinx.serialization.Serializable

@Serializable
data class Contributor(
    val id: Int? = null,
    val userId: Int,
    val project: Int
)

@Serializable
data class CreateContributorRequest(
    val userId: Int,
    val project: Int
)

@Serializable
data class UpdateContributorRequest(
    val userId: Int,
    val project: Int
)