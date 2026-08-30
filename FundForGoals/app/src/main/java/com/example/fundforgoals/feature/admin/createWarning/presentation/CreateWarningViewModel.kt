package com.example.fundforgoals.feature.admin.createWarning.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.Warning
import com.example.fundforgoals.supabase.repository.WarningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateWarningUiState(
    val projectId: Int,
    val details: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val submitSuccess: Boolean = false
)

class CreateWarningViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val warningRepository = WarningRepository()

    private val projectId: Int =
        checkNotNull(savedStateHandle.get<String>("projectId")?.toIntOrNull()) {
            "projectId nav argument is required"
        }

    private val _uiState = MutableStateFlow(
        CreateWarningUiState(projectId = projectId)
    )
    val uiState: StateFlow<CreateWarningUiState> = _uiState.asStateFlow()

    fun onAction(action: CreateWarningAction) {
        when (action) {
            is CreateWarningAction.OnDetailsChanged -> {
                _uiState.update { it.copy(details = action.value, errorMessage = null) }
            }

            CreateWarningAction.OnSubmitClick -> submitWarning()

            CreateWarningAction.OnBackClick -> Unit
        }
    }

    private fun submitWarning() {
        val state = _uiState.value
        val trimmedDetails = state.details.trim()

        if (trimmedDetails.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Enter a reason for this warning") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

            runCatching {
                warningRepository.addWarning(
                    Warning(
                        createdAt = "",
                        details = trimmedDetails,
                        projectId = projectId
                    )
                )
            }.onSuccess {
                _uiState.update {
                    it.copy(isSubmitting = false, submitSuccess = true)
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = exception.message ?: "Failed to create warning"
                    )
                }
            }
        }
    }
}