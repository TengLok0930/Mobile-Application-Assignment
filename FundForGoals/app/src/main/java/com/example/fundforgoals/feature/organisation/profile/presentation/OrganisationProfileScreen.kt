package com.example.fundforgoals.feature.organisation.profile.presentation

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundforgoals.R
import com.example.fundforgoals.app.navigation.AppBottomBar
import com.example.fundforgoals.app.navigation.AppNavigationRail
import com.example.fundforgoals.core.ui.theme.BrandAccentDark
import com.example.fundforgoals.core.ui.theme.BrandAccentLight
import com.example.fundforgoals.core.util.ContentType

@Composable
fun OrganisationProfileScreen(
    uiState: OrganisationProfileUiState,
    contentType: ContentType,
    onAction: (OrganisationProfileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    when (contentType) {
        ContentType.LIST_ONLY -> {
            OrganisationProfileCompactScreen(
                uiState = uiState,
                onAction = onAction,
                modifier = modifier
            )
        }

        ContentType.LIST_AND_DETAIL -> {
            OrganisationProfileExpandedScreen(
                uiState = uiState,
                onAction = onAction,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun OrganisationProfileCompactScreen(
    uiState: OrganisationProfileUiState,
    onAction: (OrganisationProfileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomBar(
                selectedItem = "profile",
                onMessagesClick = { onAction(OrganisationProfileAction.OnMessagesClick) },
                onHomeClick = { onAction(OrganisationProfileAction.OnHomeClick) },
                onProfileClick = { onAction(OrganisationProfileAction.OnProfileClick) }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            OrganisationProfileContent(
                uiState = uiState,
                onAction = onAction,
                showBackButton = true,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            )
        }
    }
}

@Composable
private fun OrganisationProfileExpandedScreen(
    uiState: OrganisationProfileUiState,
    onAction: (OrganisationProfileAction) -> Unit,
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
                selectedItem = "profile",
                onMessagesClick = { onAction(OrganisationProfileAction.OnMessagesClick) },
                onHomeClick = { onAction(OrganisationProfileAction.OnHomeClick) },
                onProfileClick = { onAction(OrganisationProfileAction.OnProfileClick) }
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
                OrganisationProfileContent(
                    uiState = uiState,
                    onAction = onAction,
                    showBackButton = true,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                )
            }
        }
    }
}

@Composable
private fun OrganisationProfileContent(
    uiState: OrganisationProfileUiState,
    onAction: (OrganisationProfileAction) -> Unit,
    showBackButton: Boolean,
    modifier: Modifier = Modifier
) {
    val accentColor = if (isSystemInDarkTheme()) BrandAccentDark else BrandAccentLight

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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (showBackButton) {
                        IconButton(
                            onClick = { onAction(OrganisationProfileAction.OnBackClick) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_back_40px),
                                contentDescription = "Back"
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    TextButton(
                        onClick = { onAction(OrganisationProfileAction.OnLogoutClick) }
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

                OrganisationProfileHeader(
                    organisationName = uiState.organisationName
                )

                Spacer(modifier = Modifier.height(48.dp))

                OrganisationLinkRow(
                    title = "Past Projects",
                    actionText = "View >>",
                    accentColor = accentColor,
                    onClick = { onAction(OrganisationProfileAction.OnViewPastProjectsClick) }
                )

                Spacer(modifier = Modifier.height(28.dp))

                OrganisationLinkRow(
                    title = "Contributions",
                    actionText = "View >>",
                    accentColor = accentColor,
                    onClick = { onAction(OrganisationProfileAction.OnViewContributionsClick) }
                )

                Spacer(modifier = Modifier.height(28.dp))

                OrganisationSettingRow(
                    title = "Appearance",
                    value = uiState.appearanceLabel,
                    onClick = { onAction(OrganisationProfileAction.OnAppearanceClick) }
                )

                Spacer(modifier = Modifier.height(28.dp))

                OrganisationSettingRow(
                    title = "Notifications",
                    value = uiState.notificationsLabel,
                    onClick = { onAction(OrganisationProfileAction.OnNotificationsClick) }
                )

                Spacer(modifier = Modifier.height(40.dp))

                TextButton(
                    onClick = { onAction(OrganisationProfileAction.OnChangePasswordClick) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Change Password",
                        color = accentColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun OrganisationProfileHeader(
    organisationName: String
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
                    text = organisationName.firstOrNull()?.uppercase() ?: "O",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = organisationName,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun OrganisationLinkRow(
    title: String,
    actionText: String,
    accentColor: androidx.compose.ui.graphics.Color,
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

        TextButton(onClick = onClick) {
            Text(
                text = actionText,
                color = accentColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun OrganisationSettingRow(
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