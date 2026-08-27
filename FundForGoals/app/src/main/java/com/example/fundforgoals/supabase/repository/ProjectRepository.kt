package com.example.fundforgoals.supabase.repository

import com.example.fundforgoals.supabase.model.CreateProjectRequest
import com.example.fundforgoals.supabase.model.Project
import com.example.fundforgoals.supabase.model.UpdateProjectRequest
import com.example.fundforgoals.supabase.supabase
import io.github.jan.supabase.postgrest.from


class ProjectRepository {

    suspend fun getProjects(): List<Project> {
        return supabase
            .from("project")
            .select()
            .decodeList<Project>()
    }

    suspend fun addProject(project: Project) {
        val request = CreateProjectRequest(
            title = project.title,
            desc = project.desc,
            createdBy = project.createdBy,
            fundGoal = project.fundGoal,
            currentFund = project.currentFund
        )

        supabase
            .from("project")
            .insert(request)
    }

    suspend fun modifyProject(project: Project) {
        val projectId = project.id
            ?: throw IllegalArgumentException("Project ID is required for updating")

        val request = UpdateProjectRequest(
            title = project.title,
            desc = project.desc,
            fundGoal = project.fundGoal,
            currentFund = project.currentFund
        )

        supabase
            .from("project")
            .update(request) {
                filter {
                    eq("id", projectId)
                }
            }
    }

    suspend fun deleteProject(id: Int) {
        supabase
            .from("project")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}
