package com.example.fundforgoals.supabase.repository

import com.example.fundforgoals.supabase.model.CreateProjectRequest
import com.example.fundforgoals.supabase.model.Project
import com.example.fundforgoals.supabase.model.UpdateProjectRequest
import com.example.fundforgoals.supabase.model.User
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
            avatarUrl = project.avatarUrl,
            status = project.status,
            hasCert = project.hasCert
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
            avatarUrl = project.avatarUrl,
            status = project.status,
            hasCert = project.hasCert
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

    suspend fun getProjectsByUser(userId: Int): List<Project> {
        return supabase
            .from("project")
            .select {
                filter {
                    eq("created_by", userId)
                }
            }
            .decodeList<Project>()
    }

    suspend fun getProjectById(id: Int): Project? {
        return supabase
            .from("project")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeList<Project>()
            .firstOrNull()
    }

    suspend fun getOngoingProjects(): List<Project> {
        return supabase
            .from("project")
            .select {
                filter {
                    eq("status", "Ongoing")
                }
            }
            .decodeList<Project>()
    }

    suspend fun getProjectsByIds(ids: List<Int>): List<Project> {
        if (ids.isEmpty()) return emptyList()
        return supabase
            .from("project")
            .select {
                filter {
                    isIn("id", ids)
                }
            }
            .decodeList<Project>()
    }
}
