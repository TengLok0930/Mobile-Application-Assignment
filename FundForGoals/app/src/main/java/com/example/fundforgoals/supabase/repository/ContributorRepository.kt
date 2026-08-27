package com.example.fundforgoals.supabase.repository

import com.example.fundforgoals.supabase.model.Contributor
import com.example.fundforgoals.supabase.model.CreateContributorRequest
import com.example.fundforgoals.supabase.model.CreateProjectRequest
import com.example.fundforgoals.supabase.supabase
import io.github.jan.supabase.postgrest.from

class ContributorRepository {
    suspend fun getContributors(): List<Contributor> {
        return supabase
            .from("contributor")
            .select()
            .decodeList<Contributor>()
    }

    suspend fun addContributor(contributor : Contributor) {
        val request = CreateContributorRequest(
            project = contributor.project,
            userId = contributor.userId
        )

        supabase
            .from("contributor")
            .insert(request)
    }

    suspend fun modifyContributor(contributor : Contributor) {
        val contributorId = contributor.id
            ?: throw IllegalArgumentException("Contributor ID is required for updating")

        val request = CreateContributorRequest(
            project = contributor.project,
            userId = contributor.userId
        )

        supabase
            .from("contributor")
            .update(request) {
                filter {
                    eq("id", contributorId)
                }
            }
    }

    suspend fun deleteContributors(contributorId: Int) {
        supabase
            .from("contributor")
            .delete {
                filter {
                    eq("id", contributorId)
                }
            }
    }
}