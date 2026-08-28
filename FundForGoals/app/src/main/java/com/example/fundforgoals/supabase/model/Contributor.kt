package com.example.fundforgoals.supabase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Contributor(
    val id: Int? = null,
    @SerialName("userid")
    val userId: Int,
    val project: Int
)

@Serializable
data class CreateContributorRequest(
    @SerialName("userid")
    val userId: Int,
    val project: Int
)

@Serializable
data class UpdateContributorRequest(
    @SerialName("userid")
    val userId: Int,
    val project: Int
)