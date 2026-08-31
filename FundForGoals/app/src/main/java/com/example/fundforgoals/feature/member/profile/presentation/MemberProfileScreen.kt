package com.example.fundforgoals.feature.member.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.fundforgoals.app.navigation.AppBottomBar
import com.example.fundforgoals.app.navigation.AppNavigationRail
import com.example.fundforgoals.core.ui.theme.BrandAccentDark
import com.example.fundforgoals.core.ui.theme.BrandAccentLight
import com.example.fundforgoals.core.util.ContentType
import coil.compose.AsyncImage
import com.example.fundforgoals.R

@Composable
fun MemberProfileScreen(
    uiState: MemberProfileUiState,
    contentType: ContentType,
    onAction: (MemberProfileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    when (contentType) {
        ContentType.LIST_ONLY -> MemberProfileCompactScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )

        ContentType.LIST_AND_DETAIL -> MemberProfileExpandedScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    }
}

@Composable
private fun MemberProfileCompactScreen(
    uiState: MemberProfileUiState,
    onAction: (MemberProfileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var showContributions by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomBar(
                selectedItem = "profile",
                onMessagesClick = { onAction(MemberProfileAction.OnMessagesClick) },
                onHomeClick = { onAction(MemberProfileAction.OnHomeClick) },
                onProfileClick = { onAction(MemberProfileAction.OnProfileClick) }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            if (!showContributions) {
                MemberProfileMainPane(
                    uiState = uiState,
                    onAction = { action ->
                        if (action == MemberProfileAction.OnViewContributionsClick) {
                            showContributions = true
                        } else {
                            onAction(action)
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    showContributionHeader = false
                )
            } else {
                Column(modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)) {
                    IconButton(
                        onClick = { showContributions = false }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_40px),
                            contentDescription = "Back"
                        )
                    }

                    MemberProfileContributionPane(
                        uiState = uiState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MemberProfileExpandedScreen(
    uiState: MemberProfileUiState,
    onAction: (MemberProfileAction) -> Unit,
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
                onMessagesClick = { onAction(MemberProfileAction.OnMessagesClick) },
                onHomeClick = { onAction(MemberProfileAction.OnHomeClick) },
                onProfileClick = { onAction(MemberProfileAction.OnProfileClick) }
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
                MemberProfileMainPane(
                    uiState = uiState,
                    onAction = onAction,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    showContributionHeader = true
                )
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
                MemberProfileContributionPane(
                    uiState = uiState,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun MemberProfileMainPane(
    uiState: MemberProfileUiState,
    onAction: (MemberProfileAction) -> Unit,
    showContributionHeader: Boolean,
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
                    color = MaterialTheme.colorScheme.error
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
                        onClick = { onAction(MemberProfileAction.OnLogoutClick) }
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

                MemberProfileHeader(
                    memberName = uiState.memberName,
                    memberAvatar = uiState.memberAvatar
                )

                Spacer(modifier = Modifier.height(40.dp))

                if (!showContributionHeader) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Contributions",
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )

                        TextButton(
                            onClick = { onAction(MemberProfileAction.OnViewContributionsClick) }
                        ) {
                            Text(
                                text = "View >>",
                                color = accentColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))
                }

                MemberProfileSettingRow(
                    title = "Appearance",
                    value = if (uiState.isDarkMode) "Dark" else "Light",
                    onClick = { onAction(MemberProfileAction.OnToggleTheme) }
                )

                Spacer(modifier = Modifier.height(28.dp))

                MemberProfileSettingRow(
                    title = "Notifications",
                    value = if (uiState.notificationsEnabled) "On" else "Off",
                    onClick = { onAction(MemberProfileAction.OnToggleNotifications) }
                )

                Spacer(modifier = Modifier.height(40.dp))

                TextButton(
                    onClick = { onAction(MemberProfileAction.OnChangePasswordClick) },
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
            }
        }
    }
}

@Composable
private fun MemberProfileContributionPane(
    uiState: MemberProfileUiState,
    modifier: Modifier = Modifier
) {
    var selectedECertContribution by remember { mutableStateOf<MemberContributionUi?>(null) }

    selectedECertContribution?.let { contribution ->
        ECertificateDialog(
            memberName = uiState.memberName,
            contribution = contribution,
            onDismiss = { selectedECertContribution = null }
        )
    }

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
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Contributions",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                item {
                    Text(
                        text = "Ongoing",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                items(uiState.ongoingContributions) { contribution ->
                    MemberContributionCard(
                        contribution = contribution,
                        onViewECertClick = { selectedECertContribution = it }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Past",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                items(uiState.pastContributions) { contribution ->
                    MemberContributionCard(
                        contribution = contribution,
                        onViewECertClick = { selectedECertContribution = it }
                    )
                }
            }
        }
    }
}

@Composable
private fun MemberProfileHeader(
    memberName: String,
    memberAvatar: String
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
                if (memberAvatar.isNotBlank()) {
                    AsyncImage(
                        model = memberAvatar,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                    Text(
                        text = memberName.firstOrNull()?.uppercase() ?: "M",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = memberName,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun MemberProfileSettingRow(
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

@Composable
private fun MemberContributionCard(
    contribution: MemberContributionUi,
    onViewECertClick: (MemberContributionUi) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contribution.projectTitle.take(1),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = contribution.projectTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = contribution.organisationName,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (contribution.isOngoing) {
                    Text(
                        text = "Contributed ${contribution.amountText}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                } else if (contribution.hasECertificate) {
                    TextButton(
                        onClick = { onViewECertClick(contribution) },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "View e-cert",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ECertificateDialog(
    memberName: String,
    contribution: MemberContributionUi,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 28.dp,
                    vertical = 32.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CERTIFICATE OF CONTRIBUTION",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = contribution.projectTitle,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "This certificate is proudly presented to",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = memberName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Thank you $memberName for contributing ${contribution.amountText}",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your support helps make this project possible.",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                TextButton(
                    onClick = onDismiss
                ) {
                    Text(
                        text = "Close",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}