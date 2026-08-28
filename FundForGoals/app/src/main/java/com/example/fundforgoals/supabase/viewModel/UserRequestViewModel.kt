package com.example.fundforgoals.supabase.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.UserRequest
import com.example.fundforgoals.supabase.repository.UserRequestRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UserRequestUiState(
    val isLoading: Boolean = false,
    val projectRequests: List<UserRequest> = emptyList(),
    val error: String? = null
)

class UserRequestViewModel : ViewModel() {

    private val repository = UserRequestRepository()

    private val _uiState = MutableStateFlow(UserRequestUiState())
    val uiState: StateFlow<UserRequestUiState> = _uiState.asStateFlow()

    init {
        loadUserRequests()
    }

    fun loadUserRequests() {
        viewModelScope.launch {
            executeRequest {
                repository.getUserRequests()
            }?.let { userRequests ->
                _uiState.value = UserRequestUiState(
                    isLoading = false,
                    projectRequests = userRequests
                )
            }
        }
    }

    fun addUserRequest(userRequest: UserRequest) {
        viewModelScope.launch {
            executeAction {
                repository.addUserRequest(userRequest)
            }
        }
    }

    fun modifyUserRequest(userRequest: UserRequest) {
        viewModelScope.launch {
            executeAction {
                repository.modifyUserRequest(userRequest)
            }
        }
    }

    fun deleteUserRequest(id: Int) {
        viewModelScope.launch {
            executeAction {
                repository.deleteUserRequest(id)
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

            val userRequests = repository.getUserRequests()

            _uiState.value = UserRequestUiState(
                isLoading = false,
                projectRequests = userRequests
            )
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = exception.message ?: "Operation failed"
            )
        }
    }

    private suspend fun executeRequest(
        request: suspend () -> List<UserRequest>
    ): List<UserRequest>? {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        return try {
            request()
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = exception.message ?: "Unable to load user requests"
            )
            null
        }
    }
}