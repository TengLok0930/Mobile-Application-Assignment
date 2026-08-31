package com.example.fundforgoals.feature.admin.profile.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundforgoals.app.navigation.AdminBottomBar
import com.example.fundforgoals.app.navigation.AdminNavigationRail
import com.example.fundforgoals.core.ui.theme.BrandAccentDark
import com.example.fundforgoals.core.ui.theme.BrandAccentLight
import com.example.fundforgoals.core.util.ContentType

@Composable
fun AdminProfileScreen(
    uiState: AdminProfileUiState,
    contentType: ContentType,
    onAction: (AdminProfileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    when (contentType) {
        ContentType.LIST_ONLY -> {
            AdminProfileCompactScreen(
                uiState = uiState,
                onAction = onAction,
                modifier = modifier
            )
        }

        ContentType.LIST_AND_DETAIL -> {
            AdminProfileExpandedScreen(
                uiState = uiState,
                onAction = onAction,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun AdminProfileCompactScreen(
    uiState: AdminProfileUiState,
    onAction: (AdminProfileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AdminBottomBar(
                selectedItem = "profile",
                onRequestsClick = { onAction(AdminProfileAction.OnRequestsClick) },
                onHomeClick = { onAction(AdminProfileAction.OnHomeClick) },
                onProfileClick = { onAction(AdminProfileAction.OnProfileClick) }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            AdminProfileContent(
                uiState = uiState,
                onAction = onAction,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            )
        }
    }
}

@Composable
private fun AdminProfileExpandedScreen(
    uiState: AdminProfileUiState,
    onAction: (AdminProfileAction) -> Unit,
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
                selectedItem = "profile",
                onRequestsClick = { onAction(AdminProfileAction.OnRequestsClick) },
                onHomeClick = { onAction(AdminProfileAction.OnHomeClick) },
                onProfileClick = { onAction(AdminProfileAction.OnProfileClick) }
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
                AdminProfileContent(
                    uiState = uiState,
                    onAction = onAction,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                )
            }
        }
    }
}

@Composable
private fun AdminProfileContent(
    uiState: AdminProfileUiState,
    onAction: (AdminProfileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = if (isSystemInDarkTheme()) {
        BrandAccentDark
    } else {
        BrandAccentLight
    }

    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading...",
                    color = MaterialTheme.colorScheme.onSurface
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
                    fontSize = 18.sp
                )
            }
        }

        else -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { onAction(AdminProfileAction.OnLogoutClick) }
                    ) {
                        Text(
                            text = "Logout",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                AdminProfileHeader(
                    adminName = uiState.adminName
                )

                Spacer(modifier = Modifier.height(48.dp))

                AdminProfileSettingRow(
                    title = "Appearance",
                    value = uiState.appearanceLabel,
                    onClick = { onAction(AdminProfileAction.OnAppearanceClick) }
                )

                Spacer(modifier = Modifier.height(28.dp))

                AdminProfileSettingRow(
                    title = "Notifications",
                    value = uiState.notificationsLabel,
                    onClick = { onAction(AdminProfileAction.OnNotificationsClick) }
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun AdminProfileHeader(
    adminName: String
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = adminName.firstOrNull()?.uppercase() ?: "A",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = adminName,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun AdminProfileSettingRow(
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )

        Button(
            onClick = onClick,
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}