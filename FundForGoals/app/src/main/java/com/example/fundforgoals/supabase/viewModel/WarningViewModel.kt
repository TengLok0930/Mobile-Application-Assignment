package com.example.fundforgoals.supabase.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.Warning
import com.example.fundforgoals.supabase.repository.WarningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WarningUiState(
    val isLoading: Boolean = false,
    val warnings: List<Warning> = emptyList(),
    val error: String? = null
)

class WarningViewModel : ViewModel() {

    private val repository = WarningRepository()

    private val _uiState = MutableStateFlow(WarningUiState())
    val uiState: StateFlow<WarningUiState> = _uiState.asStateFlow()

    init {
        loadWarnings()
    }

    fun loadWarnings() {
        viewModelScope.launch {
            executeRequest {
                repository.getWarnings()
            }?.let { warnings ->
                _uiState.value = WarningUiState(
                    isLoading = false,
                    warnings = warnings
                )
            }
        }
    }

    fun addWarning(warning: Warning) {
        viewModelScope.launch {
            executeAction {
                repository.addWarning(warning)
            }
        }
    }

    fun modifyWarning(warning: Warning) {
        viewModelScope.launch {
            executeAction {
                repository.modifyWarning(warning)
            }
        }
    }

    fun deleteWarning(id: Int) {
        viewModelScope.launch {
            executeAction {
                repository.deleteWarning(id)
            }
        }
    }

    private suspend fun executeAction(
        action: suspend () -> Unit
    ) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        try {
            action()

            val warnings = repository.getWarnings()

            _uiState.value = WarningUiState(
                isLoading = false,
                warnings = warnings
            )
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = exception.message ?: "Operation failed"
            )
        }
    }

    private suspend fun executeRequest(
        request: suspend () -> List<Warning>
    ): List<Warning>? {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        return try {
            request()
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = exception.message ?: "Unable to load warnings"
            )
            null
        }
    }
}