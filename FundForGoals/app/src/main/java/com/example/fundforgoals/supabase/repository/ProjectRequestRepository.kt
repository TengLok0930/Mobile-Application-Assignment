package com.example.fundforgoals.supabase.repository

import com.example.fundforgoals.supabase.model.CreateProjectRequestRequest
import com.example.fundforgoals.supabase.model.ProjectRequest
import com.example.fundforgoals.supabase.model.UpdateProjectRequestRequest
import com.example.fundforgoals.supabase.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class ProjectRequestRepository {

    suspend fun getProjectRequests(): List<ProjectRequest> {
        return supabase
            .from("project_request")
            .select(
                Columns.raw(
                    """
                id,
                created_at,
                details,
                ai_overview,
                status,
                project_id,
                project:project (
                    id,
                    title
                )
                """.trimIndent()
                )
            ) {
                filter {
                    eq("status", "pending")
                }
            }
            .decodeList<ProjectRequest>()
    }

    suspend fun addProjectRequest(projectRequest: ProjectRequest) {
        val request = CreateProjectRequestRequest(
            details = projectRequest.details,
            aiOverview = projectRequest.aiOverview,
            status = projectRequest.status,
            projectId = projectRequest.projectId
        )

        supabase
            .from("project_request")
            .insert(request)
    }

    suspend fun modifyProjectRequest(projectRequest: ProjectRequest): ProjectRequest {
        val projectRequestId = projectRequest.id
            ?: throw IllegalArgumentException("Project Request ID is required for updating")

        val request = UpdateProjectRequestRequest(
            details = projectRequest.details,
            aiOverview = projectRequest.aiOverview,
            status = projectRequest.status
        )

        return supabase
            .from("project_request")
            .update(request) {
                filter {
                    eq("id", projectRequestId)
                }
                select()
            }
            .decodeSingle<ProjectRequest>()
    }

    suspend fun updateProjectRequestStatus(
        id: Int,
        status: String
    ) {
        supabase
            .from("project_request")
            .update(
                mapOf("status" to status)
            ) {
                filter {
                    eq("id", id)
                }
            }
    }

    suspend fun deleteProjectRequest(id: Int) {
        supabase
            .from("project_request")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}