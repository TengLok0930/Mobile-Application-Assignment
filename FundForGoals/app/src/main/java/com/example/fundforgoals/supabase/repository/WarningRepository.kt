package com.example.fundforgoals.supabase.repository

import com.example.fundforgoals.supabase.model.CreateWarningRequest
import com.example.fundforgoals.supabase.model.UpdateWarningRequest
import com.example.fundforgoals.supabase.model.Warning
import com.example.fundforgoals.supabase.supabase
import io.github.jan.supabase.postgrest.from

class WarningRepository {

    suspend fun getWarnings(): List<Warning> {
        return supabase
            .from("warning")
            .select()
            .decodeList<Warning>()
    }

    suspend fun addWarning(warning: Warning) {
        val request = CreateWarningRequest(
            details = warning.details,
            projectId = warning.projectId
        )

        supabase
            .from("warning")
            .insert(warning)
    }

    suspend fun modifyWarning(warning: Warning) {
        val warningId = warning.id
            ?: throw IllegalArgumentException("Warning ID is required for updating")

        val request = UpdateWarningRequest(
            details = warning.details
        )

        supabase
            .from("warning")
            .update(request) {
                filter {
                    eq("id", warningId)
                }
            }
    }

    suspend fun deleteWarning(id: Int) {
        supabase
            .from("warning")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}