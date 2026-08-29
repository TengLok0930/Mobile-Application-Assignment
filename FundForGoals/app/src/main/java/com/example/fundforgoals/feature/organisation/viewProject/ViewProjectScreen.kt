package com.example.fundforgoals.feature.organisation.viewProject

import android.view.View
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fundforgoals.R
import com.example.fundforgoals.app.navigation.AppBottomBar
import com.example.fundforgoals.app.navigation.AppNavigationRail
import com.example.fundforgoals.core.ui.components.input.SearchBar
import com.example.fundforgoals.core.ui.theme.BrandAccentDark
import com.example.fundforgoals.core.ui.theme.BrandAccentLight
import com.example.fundforgoals.supabase.model.Project
import kotlin.text.orEmpty

@Composable
fun ViewProjectScreen(
    uiState: ViewProjectUiState,
    onAction: (ViewProjectAction) -> Unit,
    onContributeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true
) {
    if (isCompact) {
        ViewProjectCompactScreen(
            uiState = uiState,
            onAction = onAction,
            onContributeClick = onContributeClick,
            modifier = modifier
        )
    } else {
        ViewProjectExpandedScreen(
            uiState = uiState,
            onAction = onAction,
            onContributeClick = onContributeClick,
            modifier = modifier
        )
    }
}

@Composable
fun ViewProjectCompactScreen(
    uiState: ViewProjectUiState,
    onAction: (ViewProjectAction) -> Unit,
    onContributeClick: (Int) -> Unit,
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
            if (!showDetail) {
                SearchBar(
                    value = uiState.searchQuery,
                    onValueChange = {
                        onAction(ViewProjectAction.OnSearchQueryChanged(it))
                    }
                )
            }
        },
        bottomBar = {
            AppBottomBar(
                selectedItem = "home",
                onMessagesClick = { onAction(ViewProjectAction.OnMessagesClick) },
                onHomeClick = { onAction(ViewProjectAction.OnHomeClick) },
                onProfileClick = { onAction(ViewProjectAction.OnProfileClick) }
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
                ViewProjectContent(
                    uiState = uiState,
                    onProjectSelect = { projectId ->
                        selectedProjectId = projectId
                        showDetail = true
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    showSelection = false
                )
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
                        creatorName = selectedProject
                            ?.let { project -> uiState.creatorNames[project.createdBy] }
                            .orEmpty(),
                        currentFund = selectedProject?.id
                            ?.let { uiState.projectFunds[it] }
                            ?: 0.0,
                        onContributeClick = onContributeClick
                    )
                }
            }
        }
    }
}

@Composable
fun ViewProjectExpandedScreen(
    uiState: ViewProjectUiState,
    onAction: (ViewProjectAction) -> Unit,
    onContributeClick: (Int) -> Unit,
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
                onMessagesClick = { onAction(ViewProjectAction.OnMessagesClick) },
                onHomeClick = { onAction(ViewProjectAction.OnHomeClick) },
                onProfileClick = { onAction(ViewProjectAction.OnProfileClick) }
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
                    SearchBar(
                        value = uiState.searchQuery,
                        onValueChange = {
                            onAction(ViewProjectAction.OnSearchQueryChanged(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ViewProjectListPane(
                        uiState = uiState,
                        modifier = Modifier.fillMaxSize(),
                        showSelection = true,
                        onProjectClick = { projectId ->
                            onAction(ViewProjectAction.OnProjectClick(projectId))
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
                    creatorName = uiState.selectedProject
                        ?.let { project -> uiState.creatorNames[project.createdBy] }
                        .orEmpty(),
                    currentFund = uiState.selectedProject?.id
                        ?.let { uiState.projectFunds[it] }
                        ?: 0.0,
                    onContributeClick = onContributeClick
                )
            }
        }
    }
}

@Composable
fun ViewProjectContent(
    uiState: ViewProjectUiState,
    onProjectSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showSelection: Boolean = false
) {
    when {
        uiState.isLoading -> { /* unchanged */ }
        uiState.errorMessage != null -> { /* unchanged */ }
        uiState.projects.isEmpty() -> { /* unchanged */ }

        else -> {
            ViewProjectListPane(
                uiState = uiState,
                modifier = modifier,
                showSelection = showSelection,
                onProjectClick = onProjectSelect
            )
        }
    }
}

@Composable
private fun ViewProjectListPane(
    uiState: ViewProjectUiState,
    onProjectClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showSelection: Boolean = false
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(
            items = uiState.projects,
            key = { project ->
                project.id ?: project.title
            }
        ) { project ->

            ProjectCard(
                project = project,
                creatorName = uiState.creatorNames[project.createdBy].orEmpty(),
                isSelected = showSelection &&
                        project.id == uiState.selectedProjectId,
                onClick = {
                    project.id?.let { projectId ->
                        onProjectClick(projectId)
                    }
                }
            )
        }
    }
}

@Composable
private fun ProjectCard(
    project: Project,
    creatorName: String,
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
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
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
                    text = "Created by user $creatorName",
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
    creatorName: String,
    currentFund: Double,
    onContributeClick: (Int) -> Unit
) {
    val accentColor = if (isSystemInDarkTheme()) BrandAccentDark else BrandAccentLight

    if (project == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Select a project",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 18.sp
            )
        }
    } else {
        val progress = if (project.fundGoal > 0.0) {
            (currentFund / project.fundGoal).toFloat().coerceIn(0f, 1f)
        } else {
            0f
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = project.title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Created by user $creatorName",
                color = accentColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Progress",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(10.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.15f
                )
            )

            Text(
                text = "Current funds: RM %.2f".format(currentFund),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Funding goal: RM %.2f".format(project.fundGoal),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = project.desc,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { project.id?.let(onContributeClick) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Contribute")
            }
        }
    }
}