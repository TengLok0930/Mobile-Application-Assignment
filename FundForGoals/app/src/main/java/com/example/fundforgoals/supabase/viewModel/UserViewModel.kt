package com.example.fundforgoals.supabase.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.User
import com.example.fundforgoals.supabase.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UserUiState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null
)

class UserViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            executeRequest {
                repository.getUsers()
            }?.let { users ->
                _uiState.value = UserUiState(
                    isLoading = false,
                    users = users
                )
            }
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            executeAction {
                repository.addUser(user)
            }
        }
    }

    fun modifyUser(user: User) {
        viewModelScope.launch {
            executeAction {
                repository.modifyUser(user)
            }
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            executeAction {
                repository.deleteUser(id)
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

            val users = repository.getUsers()

            _uiState.value = UserUiState(
                isLoading = false,
                users = users
            )
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = exception.message ?: "Operation failed"
            )
        }
    }

    private suspend fun executeRequest(
        request: suspend () -> List<User>
    ): List<User>? {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        return try {
            request()
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = exception.message ?: "Unable to load users"
            )
            null
        }
    }
}