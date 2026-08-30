package com.example.fundforgoals.feature.admin.home.presentation

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import com.example.fundforgoals.app.navigation.AdminBottomBar
import com.example.fundforgoals.app.navigation.AdminNavigationRail
import com.example.fundforgoals.core.ui.components.input.SearchBar
import com.example.fundforgoals.core.ui.theme.BrandAccentDark
import com.example.fundforgoals.core.ui.theme.BrandAccentLight

@Composable
fun AdminHomeScreen(
    uiState: AdminHomeUiState,
    onAction: (AdminHomeAction) -> Unit,
    onWarnProjectClick: (Int) -> Unit,
    onViewChatroomClick: (Int) -> Unit,
    onViewWarningClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true
) {
    if (isCompact) {
        AdminHomeCompactScreen(uiState, onAction, onWarnProjectClick, onViewChatroomClick, onViewWarningClick, modifier)
    } else {
        AdminHomeExpandedScreen(uiState, onAction, onWarnProjectClick, onViewChatroomClick, onViewWarningClick, modifier)
    }
}
@Composable
private fun AdminHomeCompactScreen(
    uiState: AdminHomeUiState,
    onAction: (AdminHomeAction) -> Unit,
    onWarnProjectClick: (Int) -> Unit,
    onViewChatroomClick: (Int) -> Unit,
    onViewWarningClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDetail by rememberSaveable { mutableStateOf(false) }
    var selectedProjectId by rememberSaveable { mutableStateOf<Int?>(null) }

    val selectedProject = uiState.projects.firstOrNull { it.id == selectedProjectId }

    BackHandler(enabled = showDetail) {
        showDetail = false
    }

    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (!showDetail) {
                SearchBar(
                    value = uiState.searchQuery,
                    onValueChange = { onAction(AdminHomeAction.OnSearchQueryChanged(it)) }
                )
            }
        },
        bottomBar = {
            if (!showDetail) {
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
            if (!showDetail) {
                AdminHomeContent(
                    uiState = uiState,
                    onAction = { action ->
                        if (action is AdminHomeAction.OnMonitorClick) {
                            selectedProjectId = action.projectId
                            showDetail = true
                        } else {
                            onAction(action)
                        }
                    },
                    showSelection = false,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
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

                    AdminProjectDetailPane(
                        project = selectedProject,
                        isCancelling = uiState.isCancelling,
                        onCancelClick = { onAction(AdminHomeAction.OnCancelProjectClick) },
                        onWarnClick = { selectedProject?.id?.let(onWarnProjectClick) },
                        onChatroomClick = { selectedProject?.id?.let(onViewChatroomClick) },
                        onViewWarningClick = { selectedProject?.id?.let(onViewWarningClick) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminHomeExpandedScreen(
    uiState: AdminHomeUiState,
    onAction: (AdminHomeAction) -> Unit,
    onWarnProjectClick: (Int) -> Unit,
    onViewChatroomClick: (Int) -> Unit,
    onViewWarningClick: (Int) -> Unit,
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
                modifier = Modifier.weight(1f).fillMaxHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    SearchBar(
                        value = uiState.searchQuery,
                        onValueChange = { onAction(AdminHomeAction.OnSearchQueryChanged(it)) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AdminHomeContent(
                        uiState = uiState,
                        onAction = onAction,
                        showSelection = true,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Card(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                AdminProjectDetailPane(
                    project = uiState.selectedProject,
                    isCancelling = uiState.isCancelling,
                    onCancelClick = { onAction(AdminHomeAction.OnCancelProjectClick) },
                    onWarnClick = { uiState.selectedProject?.id?.let(onWarnProjectClick) },
                    onChatroomClick = { uiState.selectedProject?.id?.let(onViewChatroomClick) },
                    onViewWarningClick = { uiState.selectedProject?.id?.let(onViewWarningClick) }
                )
            }
        }
    }
}

@Composable
private fun AdminHomeContent(
    uiState: AdminHomeUiState,
    onAction: (AdminHomeAction) -> Unit,
    showSelection: Boolean,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading...")
            }
        }

        uiState.errorMessage != null -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }

        uiState.projects.isEmpty() -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No projects are available!", textAlign = TextAlign.Center)
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
                        isSelected = showSelection && project.id == uiState.selectedProjectId,
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
private fun AdminProjectCard(
    project: AdminProjectUi,
    isSelected: Boolean,
    onMonitorClick: () -> Unit
) {
    val accentColor = if (isSystemInDarkTheme()) BrandAccentDark else BrandAccentLight

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
            .clickable(onClick = onMonitorClick)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(20.dp)),
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

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.title,
                    color = MaterialTheme.colorScheme.onSurface,
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
                        color = accentColor,
                        fontSize = 16.sp,
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
                        Text(text = "Monitor", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminProjectDetailPane(
    project: AdminProjectUi?,
    isCancelling: Boolean,
    onCancelClick: () -> Unit,
    onWarnClick: () -> Unit,
    onChatroomClick: () -> Unit,
    onViewWarningClick: () -> Unit
) {
    val accentColor = if (isSystemInDarkTheme()) BrandAccentDark else BrandAccentLight

    if (project == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Select a project to view monitor details.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 18.sp
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = project.title,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = project.organisation,
            color = accentColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        TextButton(
            onClick = onViewWarningClick,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = if (project.warningCount == 1) "1 warning" else "${project.warningCount} warnings",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }

        Text(
            text = "Overview",
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = project.overview,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "AI Overview",
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = project.aiOverview,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = onCancelClick,
            enabled = !isCancelling,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            if (isCancelling) {
                CircularProgressIndicator(modifier = Modifier.height(20.dp))
            } else {
                Text("Cancel Project")
            }
        }

        Button(
            onClick = onWarnClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Warn This Project")
        }

        OutlinedButton(
            onClick = onChatroomClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Chatroom")
        }
    }
}