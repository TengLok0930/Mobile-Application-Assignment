package com.example.fundforgoals.feature.member.home.presentation

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fundforgoals.app.navigation.AppBottomBar
import com.example.fundforgoals.app.navigation.AppNavigationRail
import com.example.fundforgoals.core.ui.components.input.SearchBar
import com.example.fundforgoals.core.ui.theme.BrandAccentDark
import com.example.fundforgoals.core.ui.theme.BrandAccentLight
import com.example.fundforgoals.supabase.model.Project

@Composable
fun MemberHomeScreen(
    uiState: MemberHomeUiState,
    onAction: (MemberHomeAction) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true
) {
    if (isCompact) {
        MemberHomeCompactScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    } else {
        MemberHomeExpandedScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    }
}

@Composable
private fun MemberHomeCompactScreen(
    uiState: MemberHomeUiState,
    onAction: (MemberHomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            SearchBar(
                value = uiState.searchQuery,
                onValueChange = {
                    onAction(MemberHomeAction.OnSearchQueryChanged(it))
                }
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedItem = "home",
                onMessagesClick = { onAction(MemberHomeAction.OnMessagesClick) },
                onHomeClick = { onAction(MemberHomeAction.OnHomeClick) },
                onProfileClick = { onAction(MemberHomeAction.OnProfileClick) }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            MemberHomeContent(
                uiState = uiState,
                onAction = onAction,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                showSelection = false
            )
        }
    }
}

@Composable
private fun MemberHomeExpandedScreen(
    uiState: MemberHomeUiState,
    onAction: (MemberHomeAction) -> Unit,
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
                onMessagesClick = { onAction(MemberHomeAction.OnMessagesClick) },
                onHomeClick = { onAction(MemberHomeAction.OnHomeClick) },
                onProfileClick = { onAction(MemberHomeAction.OnProfileClick) }
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
                            onAction(MemberHomeAction.OnSearchQueryChanged(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MemberHomeListPane(
                        uiState = uiState,
                        modifier = Modifier.fillMaxSize(),
                        onProjectClick = { projectId ->
                            onAction(
                                MemberHomeAction.OnProjectClick(projectId)
                            )
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
                        ?.let { project ->
                            uiState.creatorNames[project.createdBy]
                        }
                        .orEmpty()
                )
            }
        }
    }
}

@Composable
private fun MemberHomeContent(
    uiState: MemberHomeUiState,
    onAction: (MemberHomeAction) -> Unit,
    modifier: Modifier = Modifier,
    showSelection: Boolean = false
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading...",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        uiState.errorMessage != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        uiState.projects.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No projects\nare available!",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        else -> {
            MemberHomeListPane(
                uiState = uiState,
                modifier = modifier,
                showSelection = showSelection,
                onProjectClick = { projectId ->
                    onAction(
                        MemberHomeAction.OnProjectClick(projectId)
                    )
                }
            )
        }
    }
}

@Composable
private fun MemberHomeListPane(
    uiState: MemberHomeUiState,
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
    creatorName: String
) {
    val accentColor = if (isSystemInDarkTheme()) {
        BrandAccentDark
    } else {
        BrandAccentLight
    }

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
            (project.currentFund / project.fundGoal)
                .toFloat()
                .coerceIn(0f, 1f)
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
                text = "Current funds: RM %.2f".format(project.currentFund),
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

            androidx.compose.material3.Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Contribute")
            }
        }
    }
}