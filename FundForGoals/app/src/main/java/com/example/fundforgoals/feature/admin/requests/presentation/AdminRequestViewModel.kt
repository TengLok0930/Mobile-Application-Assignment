package com.example.fundforgoals.feature.admin.requests.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.ai.GeminiRepository
import com.example.fundforgoals.supabase.model.ProjectRequest
import com.example.fundforgoals.supabase.model.UserRequest
import com.example.fundforgoals.supabase.repository.ProjectRequestRepository
import com.example.fundforgoals.supabase.repository.UserRepository
import com.example.fundforgoals.supabase.repository.UserRequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AdminRequestType {
    ORGANISATION,
    USER,
    PROJECT
}

data class AdminRequestCategoryUi(
    val type: AdminRequestType,
    val title: String,
    val count: Int,
    val buttonText: String
)

data class AdminRequestItemUi(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val aiSummary: String,
    val hasAiOverview: Boolean,
    val details: String,
    val status: String,
    val createdAt: String
)

data class AdminRequestUiState(
    val categories: List<AdminRequestCategoryUi> = emptyList(),
    val selectedType: AdminRequestType? = null,
    val requestItems: List<AdminRequestItemUi> = emptyList(),
    val selectedRequestId: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val selectedRequest: AdminRequestItemUi?
        get() = requestItems.find { it.id == selectedRequestId }
}

class AdminRequestViewModel : ViewModel() {

    private val geminiRepository = GeminiRepository()
    private val userRepository = UserRepository()
    private val userRequestRepository = UserRequestRepository()
    private val projectRequestRepository = ProjectRequestRepository()

    private var rawUserRequests: List<UserRequest> = emptyList()
    private var rawProjectRequests: List<ProjectRequest> = emptyList()

    private var cachedUserRequests: List<AdminRequestItemUi> = emptyList()
    private var cachedProjectRequests: List<AdminRequestItemUi> = emptyList()
    private var cachedOrganisationRequests: List<AdminRequestItemUi> = emptyList()

    private val _uiState = MutableStateFlow(
        AdminRequestUiState(
            categories = emptyList(),
            selectedType = null,
            requestItems = emptyList(),
            selectedRequestId = null,
            isLoading = true,
            errorMessage = null
        )
    )
    val uiState: StateFlow<AdminRequestUiState> = _uiState.asStateFlow()

    init {
        loadRequests()
    }

    private fun loadRequests(
        preserveCategorySelection: Boolean = false,
        preserveSelectedRequest: Boolean = false
    ) {
        viewModelScope.launch {
            val previousSelectedType = _uiState.value.selectedType
            val previousSelectedRequestId = _uiState.value.selectedRequestId

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            val userResult = runCatching {
                userRequestRepository.getUserRequests()
            }

            val projectResult = runCatching {
                projectRequestRepository.getProjectRequests()
            }

            val userRequests = userResult.getOrElse { emptyList() }
            val projectRequests = projectResult.getOrElse { emptyList() }

            val errors = listOfNotNull(
                userResult.exceptionOrNull()?.message?.let { "User requests: $it" },
                projectResult.exceptionOrNull()?.message?.let { "Project requests: $it" }
            )

            rawUserRequests = userRequests
            rawProjectRequests = projectRequests

            cachedUserRequests = userRequests
                .filter { it.user?.userType.equals("MEMBER", ignoreCase = true) }
                .map { it.toAdminUserRequestItemUi() }

            cachedOrganisationRequests = userRequests
                .filter { it.user?.userType.equals("ORGANISATION", ignoreCase = true) }
                .map { it.toAdminOrganisationRequestItemUi() }

            cachedProjectRequests = projectRequests
                .map { it.toAdminProjectRequestItemUi() }

            val selectedType = if (preserveCategorySelection) {
                previousSelectedType
            } else {
                null
            }

            val selectedItems = when (selectedType) {
                AdminRequestType.USER -> cachedUserRequests
                AdminRequestType.ORGANISATION -> cachedOrganisationRequests
                AdminRequestType.PROJECT -> cachedProjectRequests
                null -> emptyList()
            }

            val selectedRequestId = if (
                preserveSelectedRequest &&
                previousSelectedRequestId != null &&
                selectedItems.any { it.id == previousSelectedRequestId }
            ) {
                previousSelectedRequestId
            } else {
                null
            }

            _uiState.update {
                it.copy(
                    categories = buildCategories(
                        organisationCount = cachedOrganisationRequests.size,
                        userCount = cachedUserRequests.size,
                        projectCount = cachedProjectRequests.size
                    ),
                    selectedType = selectedType,
                    requestItems = selectedItems,
                    selectedRequestId = selectedRequestId,
                    isLoading = false,
                    errorMessage = errors.takeIf { it.isNotEmpty() }?.joinToString("\n")
                )
            }
        }
    }

    fun onAction(action: AdminRequestAction) {
        when (action) {
            is AdminRequestAction.OnCategoryClick -> {
                val items = when (action.type) {
                    AdminRequestType.USER -> cachedUserRequests
                    AdminRequestType.ORGANISATION -> cachedOrganisationRequests
                    AdminRequestType.PROJECT -> cachedProjectRequests
                }

                _uiState.update {
                    it.copy(
                        selectedType = action.type,
                        requestItems = items,
                        selectedRequestId = null
                    )
                }
            }

            is AdminRequestAction.OnRequestClick -> {
                _uiState.update {
                    it.copy(selectedRequestId = action.requestId)
                }
            }

            AdminRequestAction.OnBackFromCategoryClick -> {
                _uiState.update {
                    it.copy(
                        selectedType = null,
                        requestItems = emptyList(),
                        selectedRequestId = null
                    )
                }
            }

            AdminRequestAction.OnBackFromDetailClick -> {
                _uiState.update {
                    it.copy(selectedRequestId = null)
                }
            }

            AdminRequestAction.OnGenerateAiOverviewClick -> {
                generateAiOverviewForSelectedRequest()
            }

            AdminRequestAction.OnAcceptRequestClick -> {
                updateSelectedRequestStatus("approved")
            }

            AdminRequestAction.OnRejectRequestClick -> {
                updateSelectedRequestStatus("rejected")
            }

            AdminRequestAction.OnRequestsClick -> Unit
            AdminRequestAction.OnHomeClick -> Unit
            AdminRequestAction.OnProfileClick -> Unit
        }
    }

    private fun generateAiOverviewForSelectedRequest() {
        val selectedRequestId = _uiState.value.selectedRequestId ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                when {
                    selectedRequestId.startsWith("user_") -> {
                        val requestId = selectedRequestId.removePrefix("user_").toInt()
                        val existingRequest = rawUserRequests.firstOrNull { it.id == requestId }
                            ?: throw IllegalStateException("User request not found.")

                        if (existingRequest.aiOverview.orEmpty().isNotBlank()) {
                            _uiState.update { state -> state.copy(isLoading = false) }
                            return@launch
                        }

                        val generatedOverview = geminiRepository.generateOverview(
                            existingRequest.details
                        )

                        userRequestRepository.modifyUserRequest(
                            existingRequest.copy(aiOverview = generatedOverview)
                        )
                    }

                    selectedRequestId.startsWith("org_") -> {
                        val requestId = selectedRequestId.removePrefix("org_").toInt()
                        val existingRequest = rawUserRequests.firstOrNull { it.id == requestId }
                            ?: throw IllegalStateException("Organisation request not found.")

                        if (existingRequest.aiOverview.orEmpty().isNotBlank()) {
                            _uiState.update { state -> state.copy(isLoading = false) }
                            return@launch
                        }

                        val generatedOverview = geminiRepository.generateOverview(
                            existingRequest.details
                        )

                        userRequestRepository.modifyUserRequest(
                            existingRequest.copy(aiOverview = generatedOverview)
                        )
                    }

                    selectedRequestId.startsWith("project_") -> {
                        val requestId = selectedRequestId.removePrefix("project_").toInt()
                        val existingRequest = rawProjectRequests.firstOrNull { it.id == requestId }
                            ?: throw IllegalStateException("Project request not found.")

                        if (existingRequest.aiOverview.orEmpty().isNotBlank()) {
                            _uiState.update { state -> state.copy(isLoading = false) }
                            return@launch
                        }

                        val generatedOverview = geminiRepository.generateOverview(
                            existingRequest.details
                        )

                        projectRequestRepository.modifyProjectRequest(
                            existingRequest.copy(aiOverview = generatedOverview)
                        )
                    }

                    else -> throw IllegalArgumentException("Unknown request type.")
                }

                loadRequests(
                    preserveCategorySelection = true,
                    preserveSelectedRequest = true
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to generate AI overview."
                    )
                }
            }
        }
    }

    private fun updateSelectedRequestStatus(
        newStatus: String
    ) {
        val selectedRequestId = _uiState.value.selectedRequestId ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                when {
                    selectedRequestId.startsWith("user_") -> {
                        val requestId = selectedRequestId.removePrefix("user_").toInt()
                        val existingRequest = rawUserRequests.firstOrNull { it.id == requestId }
                            ?: throw IllegalStateException("User request not found.")

                        if (existingRequest.status.equals(newStatus, ignoreCase = true)) {
                            _uiState.update { it.copy(isLoading = false) }
                            return@launch
                        }

                        userRequestRepository.modifyUserRequest(
                            existingRequest.copy(status = newStatus)
                        )

                        if (
                            existingRequest.requestType == "MEMBER_REGISTRATION" ||
                            existingRequest.requestType == "ORGANISATION_REGISTRATION"
                        ) {
                            userRepository.updateUserApproval(
                                id = existingRequest.userId,
                                isApproved = newStatus.equals("approved", ignoreCase = true)
                            )
                        }
                    }

                    selectedRequestId.startsWith("org_") -> {
                        val requestId = selectedRequestId.removePrefix("org_").toInt()
                        val existingRequest = rawUserRequests.firstOrNull { it.id == requestId }
                            ?: throw IllegalStateException("Organisation request not found.")

                        if (existingRequest.status.equals(newStatus, ignoreCase = true)) {
                            _uiState.update { it.copy(isLoading = false) }
                            return@launch
                        }

                        userRequestRepository.modifyUserRequest(
                            existingRequest.copy(status = newStatus)
                        )

                        userRepository.updateUserApproval(
                            id = existingRequest.userId,
                            isApproved = newStatus == "approved"
                        )
                    }

                    selectedRequestId.startsWith("project_") -> {
                        val requestId = selectedRequestId.removePrefix("project_").toInt()
                        val existingRequest = rawProjectRequests.firstOrNull { it.id == requestId }
                            ?: throw IllegalStateException("Project request not found.")

                        if (existingRequest.status.equals(newStatus, ignoreCase = true)) {
                            _uiState.update { it.copy(isLoading = false) }
                            return@launch
                        }

                        projectRequestRepository.modifyProjectRequest(
                            existingRequest.copy(status = newStatus)
                        )
                    }

                    else -> throw IllegalArgumentException("Unknown request type.")
                }

                loadRequests(
                    preserveCategorySelection = true,
                    preserveSelectedRequest = true
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to update request status."
                    )
                }
            }
        }
    }

    private fun buildCategories(
        organisationCount: Int,
        userCount: Int,
        projectCount: Int
    ): List<AdminRequestCategoryUi> {
        return listOf(
            AdminRequestCategoryUi(
                type = AdminRequestType.ORGANISATION,
                title = if (organisationCount == 0) {
                    "No Organisation Requests..."
                } else {
                    "Incoming Organisation Requests"
                },
                count = organisationCount,
                buttonText = "View organisation requests"
            ),
            AdminRequestCategoryUi(
                type = AdminRequestType.USER,
                title = if (userCount == 0) {
                    "No User Requests..."
                } else {
                    "Incoming User Requests"
                },
                count = userCount,
                buttonText = "View user requests"
            ),
            AdminRequestCategoryUi(
                type = AdminRequestType.PROJECT,
                title = if (projectCount == 0) {
                    "No Project Requests..."
                } else {
                    "Incoming Project Requests"
                },
                count = projectCount,
                buttonText = "View project requests"
            )
        )
    }
}

private fun UserRequest.toAdminUserRequestItemUi(): AdminRequestItemUi {
    val overview = aiOverview.orEmpty()

    return AdminRequestItemUi(
        id = "user_${id ?: 0}",
        title = user?.name ?: "Unknown user",
        subtitle = requestType.toReadableLabel(),
        aiSummary = overview.ifBlank { "No AI summary available." },
        hasAiOverview = overview.isNotBlank(),
        details = details,
        status = status,
        createdAt = createdAt
    )
}

private fun UserRequest.toAdminOrganisationRequestItemUi(): AdminRequestItemUi {
    val overview = aiOverview.orEmpty()

    return AdminRequestItemUi(
        id = "org_${id ?: 0}",
        title = user?.name ?: "Unknown organisation",
        subtitle = requestType.toReadableLabel(),
        aiSummary = overview.ifBlank { "No AI summary available." },
        hasAiOverview = overview.isNotBlank(),
        details = details,
        status = status,
        createdAt = createdAt
    )
}

private fun ProjectRequest.toAdminProjectRequestItemUi(): AdminRequestItemUi {
    val overview = aiOverview.orEmpty()

    return AdminRequestItemUi(
        id = "project_${id ?: 0}",
        title = project?.title ?: "Unknown project",
        subtitle = null,
        aiSummary = overview.ifBlank { "No AI summary available." },
        hasAiOverview = overview.isNotBlank(),
        details = details,
        status = status,
        createdAt = createdAt
    )
}

private fun String.toReadableLabel(): String {
    return lowercase()
        .split("_")
        .joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }
}