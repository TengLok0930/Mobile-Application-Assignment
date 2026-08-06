package com.example.fundforgoals.feature.admin.home.presentation

import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundforgoals.app.navigation.AdminBottomBar
import com.example.fundforgoals.app.navigation.AdminNavigationRail
import com.example.fundforgoals.core.ui.components.input.SearchBar
import com.example.fundforgoals.core.ui.theme.BrandAccentDark
import com.example.fundforgoals.core.ui.theme.BrandAccentLight
import com.example.fundforgoals.core.ui.theme.FundForGoalsTheme

@Composable
fun AdminHomeScreen(
    uiState: AdminHomeUiState,
    onAction: (AdminHomeAction) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true
) {
    if (isCompact) {
        AdminHomeCompactScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    } else {
        AdminHomeExpandedScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    }
}

@Composable
private fun AdminHomeCompactScreen(
    uiState: AdminHomeUiState,
    onAction: (AdminHomeAction) -> Unit,
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
                    onAction(AdminHomeAction.OnSearchQueryChanged(it))
                }
            )
        },
        bottomBar = {
            AdminBottomBar(
                selectedItem = "home",
                onRequestsClick = { onAction(AdminHomeAction.OnRequestClick) },
                onHomeClick = { onAction(AdminHomeAction.OnHomeClick) },
                onProfileClick = { onAction(AdminHomeAction.OnProfileClick) }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            AdminHomeContent(
                uiState = uiState,
                onAction = onAction,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun AdminHomeExpandedScreen(
    uiState: AdminHomeUiState,
    onAction: (AdminHomeAction) -> Unit,
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
            AdminNavigationRail(
                selectedItem = "home",
                onRequestsClick = { onAction(AdminHomeAction.OnRequestClick) },
                onHomeClick = { onAction(AdminHomeAction.OnHomeClick) },
                onProfileClick = { onAction(AdminHomeAction.OnProfileClick) }
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
                            onAction(AdminHomeAction.OnSearchQueryChanged(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AdminProjectList(
                        projects = uiState.projects,
                        modifier = Modifier.fillMaxSize(),
                        onMonitorClick = { projectId ->
                            onAction(AdminHomeAction.OnMonitorClick(projectId))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminHomeContent(
    uiState: AdminHomeUiState,
    onAction: (AdminHomeAction) -> Unit,
    modifier: Modifier = Modifier
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
            AdminProjectList(
                projects = uiState.projects,
                modifier = modifier,
                onMonitorClick = { projectId ->
                    onAction(AdminHomeAction.OnMonitorClick(projectId))
                }
            )
        }
    }
}

@Composable
private fun AdminProjectList(
    projects: List<AdminProjectUi>,
    onMonitorClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(projects, key = { it.id }) { project ->
            AdminProjectCard(
                project = project,
                onMonitorClick = { onMonitorClick(project.id) }
            )
        }
    }
}

@Composable
private fun AdminProjectCard(
    project: AdminProjectUi,
    onMonitorClick:() -> Unit
) {
    val accentColor = if (isSystemInDarkTheme()) {
        BrandAccentDark
    } else {
        BrandAccentLight
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = project.title,
                    color = accentColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = project.organisation,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = onMonitorClick,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Text(
                            text = "Monitor",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}



@Preview(name = "Compact", showBackground = true)
@Composable
private fun AdminHomeCompactPreview() {
    FundForGoalsTheme {
        AdminHomeScreen(
            uiState = AdminHomeUiState(
                projects = listOf(
                    AdminProjectUi("1", "Project 1", "Organisation 1"),
                    AdminProjectUi("2", "Project 2", "Organisation 1"),
                    AdminProjectUi("3", "Project 3", "Organisation 2")
                )
            ),
            onAction = {},
            isCompact = true
        )
    }
}

@Preview(
    name = "Expanded",
    widthDp = 1000,
    heightDp = 700,
    showBackground = true
)
@Composable
private fun AdminHomeExpandedPreview() {
    FundForGoalsTheme {
        AdminHomeScreen(
            uiState = AdminHomeUiState(
                projects = listOf(
                    AdminProjectUi("1", "Project 1", "Organisation 1"),
                    AdminProjectUi("2", "Project 2", "Organisation 1"),
                    AdminProjectUi("3", "Project 3", "Organisation 2")
                )
            ),
            onAction = {},
            isCompact = false
        )
    }
}