package com.example.fundforgoals.feature.organisation.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fundforgoals.R
import com.example.fundforgoals.app.navigation.AppBottomBar
import com.example.fundforgoals.app.navigation.AppNavigationRail
import com.example.fundforgoals.core.ui.components.input.SearchBar
import com.example.fundforgoals.core.ui.theme.BrandAccentDark
import com.example.fundforgoals.core.ui.theme.BrandAccentLight
import com.example.fundforgoals.core.ui.theme.FundForGoalsTheme
import com.example.fundforgoals.supabase.model.Project

@Composable
fun OrganisationHomeScreen(
    uiState: OrganisationHomeUiState,
    onAction: (OrganisationHomeAction) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true
) {
    if (isCompact) {
        OrganisationHomeCompactScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    } else {
        OrganisationHomeExpandedScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    }
}

@Composable
private fun OrganisationHomeCompactScreen(
    uiState: OrganisationHomeUiState,
    onAction: (OrganisationHomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDetail by rememberSaveable { mutableStateOf(false) }
    var selectedProjectId by rememberSaveable { mutableStateOf<Int?>(null) }

    val selectedProject = uiState.projects.firstOrNull { it.id == selectedProjectId }

    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.loginOrganisation,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        },
        bottomBar = {
            AppBottomBar(
                selectedItem = "home",
                onMessagesClick = { onAction(OrganisationHomeAction.OnMessagesClick) },
                onHomeClick = { onAction(OrganisationHomeAction.OnHomeClick) },
                onProfileClick = { onAction(OrganisationHomeAction.OnProfileClick) }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            if (!showDetail) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    SearchBar(
                        value = uiState.searchQuery,
                        onValueChange = {
                            onAction(OrganisationHomeAction.OnSearchQueryChanged(it))
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { onAction(OrganisationHomeAction.OnNewProjectClick) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                text = "Create New Project",
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { onAction(OrganisationHomeAction.OnViewProjectClick) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                text = "View Other Project",
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OrganisationHomeContent(
                        uiState = uiState,
                        onAction = onAction,
                        onProjectSelect = { projectId ->
                            selectedProjectId = projectId
                            showDetail = true
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        showSelection = false
                    )
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    IconButton(
                        onClick = { showDetail = false }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_40px),
                            contentDescription = "Back"
                        )
                    }

                    ProjectDetailPane(
                        project = selectedProject,
                        creatorName = selectedProject?.let { uiState.creatorNames[it.createdBy] },
                        currentFund = selectedProject?.id?.let { uiState.projectFunds[it] } ?: 0.0,
                        warningCount = selectedProject?.id?.let { uiState.projectWarningCounts[it] } ?: 0,
                        aiOverview = selectedProject?.id?.let { uiState.projectAiOverviews[it] } ?: "No AI overview available.",
                        onViewWarningsClick = { projectId ->
                            onAction(OrganisationHomeAction.OnViewWarningsClick(projectId))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun OrganisationHomeExpandedScreen(
    uiState: OrganisationHomeUiState,
    onAction: (OrganisationHomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(20.dp)
        ) {
            AppNavigationRail(
                selectedItem = "home",
                onMessagesClick = { onAction(OrganisationHomeAction.OnMessagesClick) },
                onHomeClick = { onAction(OrganisationHomeAction.OnHomeClick) },
                onProfileClick = { onAction(OrganisationHomeAction.OnProfileClick) }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.loginOrganisation,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    SearchBar(
                        value = uiState.searchQuery,
                        onValueChange = {
                            onAction(OrganisationHomeAction.OnSearchQueryChanged(it))
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { onAction(OrganisationHomeAction.OnNewProjectClick) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(text = "Create New Project")
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { onAction(OrganisationHomeAction.OnViewProjectClick) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(text = "View Other Project")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OrganisationHomeListPane(
                        uiState = uiState,
                        modifier = Modifier.fillMaxSize(),
                        showSelection = true,
                        onProjectClick = { project ->
                            project.id?.let { id ->
                                onAction(OrganisationHomeAction.OnProjectClick(id))
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                ProjectDetailPane(
                    project = uiState.selectedProject,
                    creatorName = uiState.selectedProject?.let { uiState.creatorNames[it.createdBy] },
                    currentFund = uiState.selectedProject?.id?.let { uiState.projectFunds[it] } ?: 0.0,
                    warningCount = uiState.selectedProject?.id?.let { uiState.projectWarningCounts[it] } ?: 0,
                    aiOverview = uiState.selectedProject?.id?.let { uiState.projectAiOverviews[it] } ?: "No AI overview available.",
                    onViewWarningsClick = { projectId ->
                        onAction(OrganisationHomeAction.OnViewWarningsClick(projectId))
                    }
                )
            }
        }
    }
}

@Composable
private fun OrganisationHomeContent(
    uiState: OrganisationHomeUiState,
    onAction: (OrganisationHomeAction) -> Unit,
    onProjectSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showSelection: Boolean = false
) {
    when {
        uiState.isLoading -> { /* unchanged */ }
        uiState.errorMessage != null -> { /* unchanged */ }
        uiState.projects.isEmpty() -> { /* unchanged */ }

        else -> {
            OrganisationHomeListPane(
                uiState = uiState,
                modifier = modifier,
                showSelection = showSelection,
                onProjectClick = { project ->
                    project.id?.let { id -> onProjectSelect(id) }
                }
            )
        }
    }
}

@Composable
private fun OrganisationHomeListPane(
    uiState: OrganisationHomeUiState,
    onProjectClick: (Project) -> Unit,
    modifier: Modifier = Modifier,
    showSelection: Boolean = false
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(uiState.projects, key = { it.id ?: it.hashCode() }) { project ->
            ProjectCard(
                project = project,
                creatorName = uiState.creatorNames[project.createdBy],
                isSelected = showSelection && project.id == uiState.selectedProjectId,
                onClick = { onProjectClick(project) }
            )
        }
    }
}

@Composable
private fun ProjectCard(
    project: Project,
    creatorName: String?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val accentColor = if (isSystemInDarkTheme()) {
        BrandAccentDark
    } else {
        BrandAccentLight
    }

    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
    }

    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = project.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = project.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = creatorName ?: "",
                    color = accentColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ProjectDetailPane(
    project: Project?,
    creatorName: String? = null,
    currentFund: Double = 0.0,
    warningCount: Int = 0,
    aiOverview: String = "No AI overview available.",
    onViewWarningsClick: (Int) -> Unit = {}
) {
    val accentColor = if (isSystemInDarkTheme()) {
        BrandAccentDark
    } else {
        BrandAccentLight
    }

    if (project == null) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Select a project",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 18.sp
            )
        }
    } else {
        val targetAmount = if (project.fundGoal == 0.0) 1.0 else project.fundGoal
        val progressFraction = (currentFund / targetAmount).toFloat().coerceIn(0f, 1f)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = project.title.firstOrNull()?.uppercase() ?: "P",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                Column {
                    Text(
                        text = project.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = creatorName ?: "",
                        color = accentColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    if (warningCount > 0) {
                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (warningCount == 1) "1 warning reported" else "$warningCount warnings reported",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Progress",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                )
            }

            Text(
                text = "Contributions: RM %.2f".format(currentFund),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Overview",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = project.desc,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "AI Overview",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = aiOverview,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            if (warningCount > 0) {
                Button(
                    onClick = {
                        project.id?.let(onViewWarningsClick)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text(
                        text = if (warningCount == 1) {
                            "View warning"
                        } else {
                            "View warnings"
                        }
                    )
                }
            } else {
                OutlinedButton(
                    onClick = { },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("No warnings")
                }
            }
        }
    }
}