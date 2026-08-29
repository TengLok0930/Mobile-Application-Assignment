package com.example.fundforgoals.supabase.repository

import com.example.fundforgoals.supabase.model.Contributor
import com.example.fundforgoals.supabase.model.CreateContributorRequest
import com.example.fundforgoals.supabase.model.UpdateContributorRequest
import com.example.fundforgoals.supabase.supabase
import io.github.jan.supabase.postgrest.from

class ContributorRepository {

    suspend fun getContributorsByUserId(
        userId: Int
    ): List<Contributor> {
        return supabase
            .from("contributor")
            .select {
                filter {
                    eq("userid", userId)
                }
            }
            .decodeList<Contributor>()
    }

    suspend fun addContributor(
        userId: Int,
        projectId: Int,
        fundAmount: Double
    ) {
        val request = CreateContributorRequest(
            userId = userId,
            project = projectId,
            fundAmount = fundAmount
        )

        supabase
            .from("contributor")
            .insert(request)
    }

    suspend fun modifyContributor(
        contributor: Contributor
    ) {
        val contributorId = contributor.id
            ?: throw IllegalArgumentException(
                "Contributor ID is required"
            )

        val request = UpdateContributorRequest(
            userId = contributor.userId,
            project = contributor.project,
            fundAmount = contributor.fundAmount
        )

        supabase
            .from("contributor")
            .update(request) {
                filter {
                    eq("id", contributorId)
                }
            }
    }

    suspend fun deleteContributor(
        contributorId: Int
    ) {
        supabase
            .from("contributor")
            .delete {
                filter {
                    eq("id", contributorId)
                }
            }
    }

    suspend fun getContributorsByProjectIds(projectIds: List<Int>): List<Contributor> {
        if (projectIds.isEmpty()) return emptyList()
        return supabase
            .from("contributor")
            .select {
                filter {
                    isIn("project", projectIds)
                }
            }
            .decodeList<Contributor>()
    }

    suspend fun getTotalFundsByProjectIds(projectIds: List<Int>): Map<Int, Double> {
        return getContributorsByProjectIds(projectIds)
            .groupBy { it.project }
            .mapValues { (_, contributors) -> contributors.sumOf { it.fundAmount } }
    }
}