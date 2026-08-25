package com.example.fundforgoals.feature.admin.home.presentation

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundforgoals.R
import com.example.fundforgoals.app.navigation.AdminBottomBar
import com.example.fundforgoals.app.navigation.AdminNavigationRail
import com.example.fundforgoals.core.ui.components.input.SearchBar
import com.example.fundforgoals.core.ui.theme.BrandAccentDark
import com.example.fundforgoals.core.ui.theme.BrandAccentLight
import com.example.fundforgoals.core.util.ContentType

@Composable
fun AdminHomeScreen(
    uiState: AdminHomeUiState,
    contentType: ContentType,
    onAction: (AdminHomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    when (contentType) {
        ContentType.LIST_ONLY -> AdminHomeCompactScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )

        ContentType.LIST_AND_DETAIL -> AdminHomeExpandedScreen(
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
    val showingDetail = uiState.selectedProject != null

    if (showingDetail) {
        BackHandler {
            onAction(AdminHomeAction.OnBackClick)
        }
    }

    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (!showingDetail) {
                SearchBar(
                    value = uiState.searchQuery,
                    onValueChange = { onAction(AdminHomeAction.OnSearchQueryChanged(it)) }
                )
            }
        },
        bottomBar = {
            if (!showingDetail) {
                AdminBottomBar(
                    selectedItem = "home",
                    onRequestsClick = { onAction(AdminHomeAction.OnRequestClick) },
                    onHomeClick = { onAction(AdminHomeAction.OnHomeClick) },
                    onProfileClick = { onAction(AdminHomeAction.OnProfileClick) }
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            if (!showingDetail) {
                AdminHomeListPane(
                    uiState = uiState,
                    onAction = onAction,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                )
            } else {
                AdminHomeDetailPane(
                    uiState = uiState,
                    onAction = onAction,
                    showBackButton = true,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                )
            }
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
                        onValueChange = { onAction(AdminHomeAction.OnSearchQueryChanged(it)) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AdminHomeListPane(
                        uiState = uiState,
                        onAction = onAction,
                        modifier = Modifier.fillMaxSize()
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
                AdminHomeDetailPane(
                    uiState = uiState,
                    onAction = onAction,
                    showBackButton = false,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun AdminHomeListPane(
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
                Text("Loading...")
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
                    text = "No projects are available!",
                    textAlign = TextAlign.Center
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(uiState.projects, key = { it.id }) { project ->
                    AdminProjectCard(
                        project = project,
                        onMonitorClick = {
                            onAction(AdminHomeAction.OnMonitorClick(project.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminHomeDetailPane(
    uiState: AdminHomeUiState,
    onAction: (AdminHomeAction) -> Unit,
    showBackButton: Boolean,
    modifier: Modifier = Modifier
) {
    val project = uiState.selectedProject

    if (project == null) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Select a project to view monitor details.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        return
    }

    when (uiState.activeDetailPane) {
        AdminDetailPane.MONITOR -> {
            MonitorDetailContent(
                project = project,
                onAction = onAction,
                showBackButton = showBackButton,
                modifier = modifier
            )
        }

        AdminDetailPane.WARNING -> {
            WarningDetailContent(
                project = project,
                onAction = onAction,
                showBackButton = showBackButton,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun MonitorDetailContent(
    project: AdminProjectUi,
    onAction: (AdminHomeAction) -> Unit,
    showBackButton: Boolean,
    modifier: Modifier = Modifier
) {
    val accentColor = if (isSystemInDarkTheme()) BrandAccentDark else BrandAccentLight

    Column(
        modifier = modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (showBackButton) {
            IconButton(onClick = { onAction(AdminHomeAction.OnBackClick) }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back_40px),
                    contentDescription = "Back"
                )
            }
        }

        Row(verticalAlignment = Alignment.Top) {
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

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = project.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = project.organisation,
                    color = accentColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (project.warningCount == 1) "1 warning" else "${project.warningCount} warnings",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        }

        Text(
            text = "Overview",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = project.overview,
            fontSize = 16.sp
        )

        OutlinedButton(
            onClick = { onAction(AdminHomeAction.OnCancelProjectClick) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel Project")
        }

        Button(
            onClick = { onAction(AdminHomeAction.OnWarnProjectClick) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Warn This Project")
        }

        OutlinedButton(
            onClick = { onAction(AdminHomeAction.OnViewChatroomClick) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Chatroom")
        }
    }
}

@Composable
private fun WarningDetailContent(
    project: AdminProjectUi,
    onAction: (AdminHomeAction) -> Unit,
    showBackButton: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (showBackButton) {
            IconButton(onClick = { onAction(AdminHomeAction.OnBackClick) }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back_40px),
                    contentDescription = "Back"
                )
            }
        }

        Text(
            text = project.incidentTitle,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Warning details",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
            )
        ) {
            Text(
                text = project.warningDetails,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }

        Button(
            onClick = { onAction(AdminHomeAction.OnWarnOrganisationClick) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Warn the organisation")
        }
    }
}

@Composable
private fun AdminProjectCard(
    project: AdminProjectUi,
    onMonitorClick: () -> Unit
) {
    val accentColor = if (isSystemInDarkTheme()) BrandAccentDark else BrandAccentLight

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

            Column(modifier = Modifier.weight(1f)) {
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