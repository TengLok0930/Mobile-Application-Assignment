package com.example.fundforgoals.supabase.repository

import com.example.fundforgoals.supabase.model.CreateProjectRequestRequest
import com.example.fundforgoals.supabase.model.ProjectRequest
import com.example.fundforgoals.supabase.model.UpdateProjectRequestRequest
import com.example.fundforgoals.supabase.supabase
import io.github.jan.supabase.postgrest.from

class ProjectRequestRepository {

    suspend fun getProjectRequests(): List<ProjectRequest> {
        return supabase
            .from("project_request")
            .select()
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

    suspend fun modifyProjectRequest(projectRequest: ProjectRequest) {
        val projectRequestId = projectRequest.id
            ?: throw IllegalArgumentException("Project Request ID is required for updating")

        val request = UpdateProjectRequestRequest(
            details = projectRequest.details,
            aiOverview = projectRequest.aiOverview,
            status = projectRequest.status
        )

        supabase
            .from("project_request")
            .update(request) {
                filter {
                    eq("id", projectRequestId)
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